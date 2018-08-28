package org.gmelo.hsbc.assignment.persister.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by gmelo on 18/08/18.
 */
@Profile("aggregation-persistence")
@RestController
@RequestMapping("/analytics")
public class PersistedAggregationController {

    @Value("${org.gmelo.hsbc.query.select.all}")
    private String sqlForFetchAll;
    @Resource
    private JdbcTemplate template;

    /**
     * Provide all aggregated transactions
     *
     * @return all aggregated transactions
     */
    @CrossOrigin
    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public List<Map<String, Object>> executeQuery() {
        return template.queryForList(sqlForFetchAll);
    }

}
