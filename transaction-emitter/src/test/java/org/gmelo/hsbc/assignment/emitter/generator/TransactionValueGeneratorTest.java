package org.gmelo.hsbc.assignment.emitter.generator;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class TransactionValueGeneratorTest {

    private final TransactionValueGenerator transactionValueGenerator = new TransactionValueGenerator();
    //to simplify the comparison, we removed one decimal from fifty and added one from 200
    private final BigDecimal fifty = new BigDecimal(49.9);
    private final BigDecimal twoHundered = new BigDecimal(200.1);

    @Test
    public void testGenerateTransaction() throws Exception {
        transactionValueGenerator.setHigherBound(200);
        transactionValueGenerator.setLowerBound(50);

        for (int i = 0; i < 2000000; i++) {
            BigDecimal generateValue = transactionValueGenerator.generateValue();

            if (generateValue.compareTo(fifty) < 1) {
                System.out.println(generateValue);
            }
            assertEquals(1, generateValue.compareTo(fifty));
            if (generateValue.compareTo(twoHundered) > -1) {
                System.out.println(generateValue);
            }
            assertEquals(-1, generateValue.compareTo(twoHundered));
        }
    }
}