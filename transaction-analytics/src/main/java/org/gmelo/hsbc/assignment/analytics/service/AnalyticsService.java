package org.gmelo.hsbc.assignment.analytics.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.gmelo.hsbc.assignment.analytics.QueryService;
import org.gmelo.hsbc.assignment.common.model.AggregatedTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.temporal.TemporalUnit;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.gmelo.hsbc.assignment.common.factory.InternalModelFactory.aggregatedTransactionToByteArray;
import static org.gmelo.hsbc.assignment.common.factory.InternalModelFactory.createAggregatedTransactionFromMap;

/**
 * Created by gmelo on 18/08/18.
 * <p>
 * Controls the calls to the underlying spark infrastructure as well as sends the aggregated data to
 * a rabbit exchange.
 */
@Service
public class AnalyticsService {

    @Value("${org.gmelo.hsbc.persist.fanout}")
    @NotNull
    private String persistFanoutName;
    @Value("${org.gmelo.hsbc.aggregate.sum.query}")
    private String aggregateBySumByUserForInterval;
    @Value("${org.gmelo.hsbc.all.query}")
    private String allTransactionsForInterval;

    @Resource
    private QueryService queryService;
    @Resource
    private RabbitTemplate template;

    private final Logger logger = LoggerFactory.getLogger(AnalyticsService.class);

    private final ObjectMapper objectMapper = new ObjectMapper();
    // boilerplate to facilitate the conversion to Map
    private final TypeReference<Map<String, Object>> typeReference = new TypeReference<Map<String, Object>>() {
    };


    /**
     * Execute free form queries, in real life would need sanitization
     *
     * @param sql a string query to be executed by spark
     * @return
     */
    public List<String> executeFreeFormQuery(String sql) {
        return queryService.executeQuery(sql);
    }

    /**
     * endpoint to aggregate the data that comes from mongo
     *
     * @param interval the interval to aggregate on
     * @param unit     the time unit of the interval
     * @return the aggregated data
     */
    public List<Map<String, Object>> aggregateForInterval(Long interval, TemporalUnit unit) {
        Timestamp value = getTimestampForInterval(interval, unit);
        logger.debug("Aggregating for interval:{}, for time unit:{}", interval, unit);
        List<String> returned = queryService.executeQuery(String.format(aggregateBySumByUserForInterval, value.toString()));
        logger.info("Fetched {} objects", returned.size());
        return convertStringListToJson(returned);

    }

    /**
     * endpoint to fetch all the data that comes from mongo for the interval
     *
     * @param interval the interval to aggregate on
     * @param unit     the time unit of the interval
     * @return the matching data.
     */
    public List<Map<String, Object>> allTransactionsForInterval(Long interval, TemporalUnit unit) {
        Timestamp value = getTimestampForInterval(interval, unit);
        logger.debug("fetching all transactions for interval:{}, for time unit:{}", interval, unit);
        List<String> returned = queryService.executeQuery(String.format(allTransactionsForInterval, value.toString()));
        logger.info("Fetched {} objects", returned.size());
        return convertStringListToJson(returned);
    }

    /**
     * Aggregates data using spark and sends it on a queue
     */
    public void persistAggregation(Long interval, TemporalUnit unit) {
        logger.debug("Aggregating for interval:{}, for time unit:{} and sending it to be persisted", interval, unit);
        List<Map<String, Object>> aggregate = aggregateForInterval(interval, unit);
        Timestamp from = getTimestampForInterval(interval, unit);
        Timestamp to = getCurrentTimestamp();

        StringBuilder context = new StringBuilder()
                .append("CC_SUM_FOR_")
                .append(interval)
                .append("_")
                .append(unit);

        aggregate
                .stream()
                .map(map -> createAggregatedTransactionFromMap(map, to, from, context.toString()))
                .forEach(agg -> sendToRabbit(agg));
        logger.info("Sent {} aggregations to rabbit", aggregate.size());
    }

    /*
     * Sends to Rabbit
     */
    protected void sendToRabbit(AggregatedTransaction agg) {
        template.convertAndSend(persistFanoutName, "", aggregatedTransactionToByteArray(agg));
    }

    /*
     * Fetches the timestamp for the interval
     */
    protected Timestamp getTimestampForInterval(Long interval, TemporalUnit unit) {
        Timestamp now = getCurrentTimestamp();
        return Timestamp.valueOf(now.toLocalDateTime().minus(interval, unit));
    }

    /**
     * Fetches the current timestamp to be used for the query
     *
     * @return current Timestamp
     */
    protected Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * Converts a Json message in string format to a Map<String,Object> format
     *
     * @param data a list of String json objects
     * @return a list of the objects formatted differently
     */
    protected List<Map<String, Object>> convertStringListToJson(List<String> data) {
        if (data == null) {
            return Collections.emptyList();
        }
        return data.stream()
                .map(e -> convertJsonToMap(e))
                .collect(Collectors.toList());
    }

    /*
     * converts a jason string to a map
     */
    protected Map<String, Object> convertJsonToMap(String json) {
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (IOException e) {
            logger.error("exception converting", e);
            throw new IllegalArgumentException("could not convert");
        }
    }

    protected void setPersistFanoutName(@NotNull String persistFanoutName) {
        this.persistFanoutName = persistFanoutName;
    }

    protected void setAggregateBySumByUserForInterval(String aggregateBySumByUserForInterval) {
        this.aggregateBySumByUserForInterval = aggregateBySumByUserForInterval;
    }

    protected void setAllTransactionsForInterval(String allTransactionsForInterval) {
        this.allTransactionsForInterval = allTransactionsForInterval;
    }

    protected void setQueryService(QueryService queryService) {
        this.queryService = queryService;
    }

    protected void setTemplate(RabbitTemplate template) {
        this.template = template;
    }
}
