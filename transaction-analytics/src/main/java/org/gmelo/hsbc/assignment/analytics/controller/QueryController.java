package org.gmelo.hsbc.assignment.analytics.controller;

import org.springframework.web.bind.annotation.*;
import org.gmelo.hsbc.assignment.analytics.service.AnalyticsService;

import javax.annotation.Resource;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

/**
 * Created by gmelo on 18/08/18.
 * The analytics controller, currently we are accepting @CrossOrigin calls to interface with a vue ui
 */
@RestController
@RequestMapping("/analytics")
public class QueryController {

    @Resource
    private AnalyticsService analyticsService;

    /**
     * endpoint to execute free form queries.
     *
     * @param query a string query to be run
     * @return the result of the query
     */
    @CrossOrigin
    @RequestMapping(value = "/query/freeform", method = RequestMethod.POST)
    public List<String> executeFreeformQuery(@RequestBody String query) {
        return analyticsService.executeFreeFormQuery(query);
    }

    /**
     * endpoint to aggregate the data that comes from mongo
     *
     * @param interval the interval to aggregate on
     * @param unit     the time unit of the interval
     * @return the aggregated data
     */
    @CrossOrigin
    @RequestMapping(value = "/query/aggregate/{interval}/{unit}", method = RequestMethod.GET, produces = "application/json")
    public List<Map<String,Object>> executeAggregateQuery(@PathVariable Long interval,
                                              @PathVariable ChronoUnit unit) {
        return analyticsService.aggregateForInterval(interval, unit);
    }

    /**
     * endpoint to fetch all the data that comes from mongo for the interval
     *
     * @param interval the interval to aggregate on
     * @param unit     the time unit of the interval
     * @return the matching data.
     */
    @CrossOrigin
    @RequestMapping(value = "/query/all/{interval}/{unit}", method = RequestMethod.GET)
    public List<Map<String,Object>>  executeIntervalQuery(@PathVariable Long interval,
                                             @PathVariable ChronoUnit unit) {
        return analyticsService.allTransactionsForInterval(interval, unit);
    }

    /**
     * endpoint to aggregate all the data  for the interval
     *
     * @param interval the interval to aggregate on
     * @param unit     the time unit of the interval
     */
    @CrossOrigin
    @RequestMapping(value = "/persist/aggregate/{interval}/{unit}", method = RequestMethod.GET)
    public void persistExecuteAggregateQuery(@PathVariable Long interval,
                                             @PathVariable ChronoUnit unit) {
        analyticsService.persistAggregation(interval, unit);
    }

}
