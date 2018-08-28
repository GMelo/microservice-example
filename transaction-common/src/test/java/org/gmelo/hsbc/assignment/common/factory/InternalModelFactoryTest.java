package org.gmelo.hsbc.assignment.common.factory;

import org.gmelo.hsbc.assignment.common.model.AggregatedTransaction;
import org.gmelo.hsbc.assignment.common.model.InternalTransactional;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import static org.gmelo.hsbc.assignment.common.factory.InternalModelFactory.*;
import static org.gmelo.hsbc.assignment.common.model.AggregatedTransaction.aAggregatedTransaction;
import static org.gmelo.hsbc.assignment.common.model.InternalTransactional.aInternalTransactionalTransaction;
import static org.junit.Assert.*;
import static org.gmelo.hsbc.assignment.common.model.InternalTransactional.*;
import static org.gmelo.hsbc.assignment.common.model.AggregatedTransaction.*;

public class InternalModelFactoryTest {


    @Test
    public void testInternalTransactionToByteArray() throws Exception {
        InternalTransactional created = aInternalTransactionalTransaction()
                .withFirstName("Guilherme")
                .withLastName("Melo")
                .withTransactionValue(new BigDecimal(22.5).setScale(2, BigDecimal.ROUND_UP))
                .withTs(new Timestamp(System.currentTimeMillis()))
                .withUserName("MeloG")
                .withVendor("amazon")
                .build();

        byte[] returned = internalTransactionToByteArray(created);
        InternalTransactional un = byteArrayToInternalTransactionalTransaction(returned);

        assertEquals(created, un);
    }

    @Test
    public void testAggregatedTransactionToByteArray() throws Exception {
        AggregatedTransaction aggregatedTransaction = aAggregatedTransaction()
                .withAggregatedValue(new BigDecimal(22.5).setScale(2, BigDecimal.ROUND_UP))
                .withContext("ContextName")
                .withFrom(new Timestamp(System.currentTimeMillis()))
                .withTo(new Timestamp(System.currentTimeMillis()))
                .withUserName("MeloG")
                .build();

        byte[] bytes = aggregatedTransactionToByteArray(aggregatedTransaction);
        AggregatedTransaction returned = byteArrayToAggregatedTransaction(bytes);

        assertEquals(aggregatedTransaction, returned);
    }

    @Test
    public void testCreateInternalTransactionalFromMap() {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        InternalTransactional created = aInternalTransactionalTransaction()
                .withFirstName("Guilherme")
                .withLastName("Melo")
                .withTransactionValue(new BigDecimal(22.5).setScale(2, BigDecimal.ROUND_UP))
                .withTs(ts)
                .withUserName("MeloG")
                .withVendor("amazon")
                .build();

        Map<String, Object> map = new HashMap<>();
        map.put(FIRST_NAME_FIELD_NAME, "Guilherme");
        map.put(LAST_NAME_FIELD_NAME, "Melo");
        map.put(TS_FIELD_NAME, ts.toInstant().toEpochMilli());
        map.put(TRANSACTIONAL_VALUE_FIELD_NAME, new BigDecimal(22.5).setScale(2, BigDecimal.ROUND_UP));
        map.put(InternalTransactional.USER_NAME_FIELD_NAME, "MeloG");
        map.put(VENDOR_FIELD_NAME, "amazon");
        InternalTransactional returned = createInternalTransactionalFromMap(map);
        assertEquals(created, returned);

    }

    @Test
    public void testCreateAggregatedTransactionFromMap() {
        Timestamp from = new Timestamp(System.currentTimeMillis());
        Timestamp to = new Timestamp(System.currentTimeMillis());
        AggregatedTransaction aggregatedTransaction = aAggregatedTransaction()
                .withAggregatedValue(new BigDecimal(22.5).setScale(2, BigDecimal.ROUND_UP))
                .withContext("ContextName")
                .withFrom(from)
                .withTo(to)
                .withUserName("MeloG")
                .build();
        Map<String, Object> map = new HashMap<>();
        map.put(AggregatedTransaction.USER_NAME_FIELD_NAME, "MeloG");
        map.put(VALUE_FIELD_NAME, "22.50");
        AggregatedTransaction returned = createAggregatedTransactionFromMap(map, to, from, "ContextName");
        assertEquals(aggregatedTransaction, returned);

    }

}