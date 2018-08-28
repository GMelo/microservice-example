package org.gmelo.hsbc.assignment.emitter.model;

import org.gmelo.hsbc.assignment.emitter.generator.TransactionNameGenerator;
import org.gmelo.hsbc.assignment.emitter.generator.TransactionValueGenerator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by gmelo on 15/08/18.
 */
@Service
public class CreditCardTransactionFactory {

    @Resource
    private TransactionNameGenerator transactionNameGenerator;
    @Resource
    private TransactionValueGenerator transactionValueGenerator;

    public CreditCardTransaction createTransaction() {
        return CreditCardTransaction.aCreditCardTransaction()
                .withTransactionalUser(transactionNameGenerator.getRandomTransactionUser())
                .withTransactionValue(transactionValueGenerator.generateValue())
                .withVendor(transactionNameGenerator.getRetailer())
                .build();
    }
}
