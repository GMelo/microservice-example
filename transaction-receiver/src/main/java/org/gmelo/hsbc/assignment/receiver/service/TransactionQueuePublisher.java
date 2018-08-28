package org.gmelo.hsbc.assignment.receiver.service;

import org.gmelo.hsbc.assignment.common.model.InternalTransactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

import static org.gmelo.hsbc.assignment.common.factory.InternalModelFactory.createInternalTransactionalFromMap;
import static org.gmelo.hsbc.assignment.common.factory.InternalModelFactory.internalTransactionToByteArray;

/**
 * Created by gmelo on 15/08/18.
 */
@Service
public class TransactionQueuePublisher {

    private final Logger logger = LoggerFactory.getLogger(TransactionQueuePublisher.class);
    @Resource
    private RabbitTemplate template;
    @Value("${org.gmelo.hsbc.transaction.fanout}")
    private String fanoutQueueName;

    /**
     * Publishes to the internal queue
     * I elected not to have the message be the same, to force us to convert to our own model and
     * simulate a external system.
     *
     * @param data json generated data
     */
    public void publishTransaction(Map<String, Object> data) {
        InternalTransactional internalTransactional = createInternalTransactionalFromMap(data);
        //in real life this would be debug
        logger.info("Received {}", data);
        try {
            byte[] serialized = internalTransactionToByteArray(internalTransactional);
            template.convertAndSend(fanoutQueueName, "", serialized);
        } catch (Exception e) {
            logger.error("exception trying to persist {}", internalTransactional, e);
        }

    }
}
