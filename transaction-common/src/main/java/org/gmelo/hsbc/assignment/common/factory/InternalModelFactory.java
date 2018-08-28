package org.gmelo.hsbc.assignment.common.factory;

import org.apache.commons.lang3.SerializationUtils;
import org.gmelo.hsbc.assignment.common.model.AggregatedTransaction;
import org.gmelo.hsbc.assignment.common.model.InternalTransactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Map;

import static org.gmelo.hsbc.assignment.common.model.AggregatedTransaction.aAggregatedTransaction;
import static org.gmelo.hsbc.assignment.common.model.InternalTransactional.aInternalTransactionalTransaction;
import static org.gmelo.hsbc.assignment.common.model.InternalTransactional.*;

/**
 * Created by gmelo on 16/08/18.
 * Factory to Convert Maps and byte arrays to out internal model.
 * In a real scenario, this would contain mappings for upstream and downstream values
 */
public class InternalModelFactory {

    public static InternalTransactional createInternalTransactionalFromMap(Map<String, Object> data) {

        return aInternalTransactionalTransaction()
                .withTransactionValue(new BigDecimal(String.valueOf(data.get(TRANSACTIONAL_VALUE_FIELD_NAME))))
                .withVendor((String) data.get(VENDOR_FIELD_NAME))
                .withFirstName((String) data.get(FIRST_NAME_FIELD_NAME))
                .withLastName((String) data.get(LAST_NAME_FIELD_NAME))
                .withTs(new Timestamp((Long) data.get(TS_FIELD_NAME)))
                .withUserName((String) data.get(USER_NAME_FIELD_NAME))
                .build();
    }

    public static AggregatedTransaction createAggregatedTransactionFromMap(Map<String, Object> data, Timestamp to, Timestamp from, String context) {

        return aAggregatedTransaction()
                .withContext(context)
                .withTo(to)
                .withFrom(from)
                .withAggregatedValue(new BigDecimal(String.valueOf(data.get(AggregatedTransaction.VALUE_FIELD_NAME))))
                .withUserName((String) data.get(AggregatedTransaction.USER_NAME_FIELD_NAME))
                .build();
    }

    public static byte[] internalTransactionToByteArray(InternalTransactional internalTransactional) {
        return SerializationUtils.serialize(internalTransactional);
    }

    public static byte[] aggregatedTransactionToByteArray(AggregatedTransaction aggregatedTransaction) {
        return SerializationUtils.serialize(aggregatedTransaction);
    }

    public static InternalTransactional byteArrayToInternalTransactionalTransaction(byte[] serializedInternalTransactionalTransaction) {
        return SerializationUtils.deserialize(serializedInternalTransactionalTransaction);
    }

    public static AggregatedTransaction byteArrayToAggregatedTransaction(byte[] serializedInternalTransactionalTransaction) {
        return SerializationUtils.deserialize(serializedInternalTransactionalTransaction);
    }
}