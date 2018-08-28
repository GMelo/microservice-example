package org.gmelo.hsbc.assignment.analytics.service;

import org.gmelo.hsbc.assignment.analytics.QueryService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;


import java.sql.Timestamp;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.gmelo.hsbc.assignment.common.factory.InternalModelFactory.createAggregatedTransactionFromMap;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

//@RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class AnalyticsServiceTest {


    private AnalyticsService myLauncher;
    QueryService queryService;

    @Before
    public void setup() {
        myLauncher = spy(new AnalyticsService());
        queryService = mock(QueryService.class);
        myLauncher.setQueryService(queryService);
        myLauncher.setAllTransactionsForInterval("select * from CreditCardTransaction where ts > '%s'");
        myLauncher.setAggregateBySumByUserForInterval("select userName, sum(transactionValue) as aggregatedValue from CreditCardTransaction where ts > '%s' group by userName");
    }


    @Test
    public void testExecuteFreeFormQuery() throws Exception {
        String sqlQuery = "select test from test";
        myLauncher.executeFreeFormQuery(sqlQuery);
        verify(queryService, times(1)).executeQuery(sqlQuery);
    }

    @Test
    public void testAggregateForInterval() throws Exception {
        String aggregateBySumByUserForInterval = "select userName, sum(transactionValue) as aggregatedValue from CreditCardTransaction where ts > '%s' group by userName";
        Timestamp now = new Timestamp(1534599585809L);
        doReturn(now).when(myLauncher).getTimestampForInterval(6L, ChronoUnit.HOURS);
        myLauncher.aggregateForInterval(6L, ChronoUnit.HOURS);
        verify(queryService, times(1)).executeQuery(String.format(aggregateBySumByUserForInterval, now.toString()));

    }

    @Test
    public void testAllTransactionsForInterval() throws Exception {
        String allTransactionsForInterval = "select * from CreditCardTransaction where ts > '%s'";

        Timestamp now = new Timestamp(1534599585809L);
        doReturn(now).when(myLauncher).getTimestampForInterval(6L, ChronoUnit.HOURS);
        myLauncher.allTransactionsForInterval(6L, ChronoUnit.HOURS);
        verify(queryService, times(1)).executeQuery(String.format(allTransactionsForInterval, now.toString()));
    }

    @Test
    public void testPersistAggregation() throws Exception {
        List<Map<String, Object>> jsonList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("userName", "MillerJ");
        map.put("aggregatedValue", "52081.37999999992");
        jsonList.add(map);
        map = new HashMap<>();
        map.put("userName", "JonesE");
        map.put("aggregatedValue", "55872.200000000004");
        jsonList.add(map);

        Long interval = 6L;
        Timestamp now = new Timestamp(1534599585809L);
        Timestamp then = new Timestamp(1534599565809L);

        doReturn(jsonList).when(myLauncher).aggregateForInterval(interval, ChronoUnit.HOURS);
        doReturn(now).when(myLauncher).getTimestampForInterval(interval, ChronoUnit.HOURS);//
        doReturn(then).when(myLauncher).getCurrentTimestamp();
        StringBuilder context = new StringBuilder()
                .append("CC_SUM_FOR_")
                .append(interval)
                .append("_")
                .append(ChronoUnit.HOURS);

        doNothing().when(myLauncher).sendToRabbit(any());
        myLauncher.persistAggregation(interval, ChronoUnit.HOURS);

        jsonList
                .stream()
                .map(json -> createAggregatedTransactionFromMap(json, now, then, context.toString()))
                .forEach(agg -> verify(myLauncher, times(2)).sendToRabbit(any()));

    }

    @Test
    public void testGetTimestampForInterval() throws Exception {
        Timestamp now = new Timestamp(1534599585809L);
        doReturn(now).when(myLauncher).getCurrentTimestamp();
        Timestamp returned = myLauncher.getTimestampForInterval(2L, ChronoUnit.DAYS);
        //
        assertTrue(now.after(returned));
        assertEquals(now.toLocalDateTime().getHour(), returned.toLocalDateTime().getHour());
        assertEquals(2, now.toLocalDateTime().getDayOfYear() - returned.toLocalDateTime().getDayOfYear());
        //check finer grain
        returned = myLauncher.getTimestampForInterval(2L, ChronoUnit.HOURS);
        assertTrue(now.after(returned));
        assertEquals(2, now.toLocalDateTime().getHour() - returned.toLocalDateTime().getHour());

    }

    @Test
    public void testConvertJsonToMap() throws Exception {
        String json = "{\"userName\":\"MillerJ\",\"total_transactions\":52081.37999999992}";
        Map<String, Object> actual = new HashMap<>();
        actual.put("userName", "MillerJ");
        actual.put("total_transactions", 52081.37999999992);
        Map<String, Object> returned = myLauncher.convertJsonToMap(json);
        assertEquals(actual, returned);
    }
}