package org.gmelo.hsbc.assignment.emitter;

import org.gmelo.hsbc.assignment.emitter.model.CreditCardTransactionFactory;
import org.gmelo.hsbc.assignment.emitter.service.TransactionEmitter;
import org.gmelo.hsbc.assignment.emitter.service.TransactionEmitterService;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class TransactionEmitterTest {

    @Test
    public void testExecute() throws Exception {
        RestTemplate restTemplate = mock(RestTemplate.class);
        TransactionEmitter transactionEmitter = new TransactionEmitterService();
        CreditCardTransactionFactory creditCardTransactionFactory = mock(CreditCardTransactionFactory.class);
        ReflectionTestUtils.setField(transactionEmitter, "restTemplate", restTemplate);
        ReflectionTestUtils.setField(transactionEmitter, "interval", 500);
        ReflectionTestUtils.setField(transactionEmitter, "creditCardTransactionFactory", creditCardTransactionFactory);
        transactionEmitter.start();
        Thread.sleep(5 * 1000);
        transactionEmitter.stop();
        //Using at least 10 because of gracefull stoping eventually we execute once more,
        //but this still proves the method is working as intended
        verify(restTemplate, atLeast(10)).postForObject(any(String.class), any(), any());
    }
}