package org.gmelo.hsbc.assignment.emitter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.gmelo.hsbc.assignment.emitter.model.CreditCardTransaction;
import org.gmelo.hsbc.assignment.emitter.model.TransactionalUser;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.gmelo.hsbc.assignment.emitter.model.CreditCardTransaction.aCreditCardTransaction;
import static org.junit.Assert.assertEquals;

/**
 * Created by gmelo on 21/08/18.
 * the aim of this is to provide a controlled value of the generated values to validate the aggregations
 */
public class TransactionEmitterIT {

    private final RestTemplate restTemplate = new RestTemplate();
    private String receiverEndpoint = "http://127.0.0.1:8082/receiver/endpoint";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final TypeReference<List<Map<String, Object>>> typeReference = new TypeReference<List<Map<String, Object>>>() {
    };

    @Test
    public void fullIntegrationTest() throws InterruptedException, IOException {
        TransactionalUser transactionalUser = TransactionalUser.aTransactionalUser()
                .withFirstName("Guilherme")
                .withLastName("Melo").build();

        CreditCardTransaction created = aCreditCardTransaction()
                .withTransactionalUser(transactionalUser)
                .withTransactionValue(new BigDecimal(20).setScale(2, BigDecimal.ROUND_UP))
                .withVendor("amazon")
                .build();

        TransactionalUser transactionalUser2 = TransactionalUser.aTransactionalUser()
                .withFirstName("Michelle")
                .withLastName("Paluski").build();

        CreditCardTransaction created2 = aCreditCardTransaction()
                .withTransactionalUser(transactionalUser2)
                .withTransactionValue(new BigDecimal(25.5).setScale(2, BigDecimal.ROUND_UP))
                .withVendor("ali.express")
                .build();


        for (int i = 0; i < 4; i++) {
            restTemplate.postForObject(receiverEndpoint, created, ResponseEntity.class);
            restTemplate.postForObject(receiverEndpoint, created2, ResponseEntity.class);

        }
        Thread.sleep(2000);
        String Json = restTemplate.getForObject("http://localhost:8084/analytics/query/aggregate/10/SECONDS", String.class);

        List<Map<String, Object>> returnedAggregation = objectMapper.readValue(Json, typeReference);
        assertEquals(2, returnedAggregation.size());
        double eighty = 80;
        returnedAggregation.forEach(m -> {
            String username = (String) m.get("userName");
            if (username.equals("PaluskiM")) {
                assertEquals(25.50 * 4, m.get("aggregatedValue"));
            } else {
                assertEquals(eighty, m.get("aggregatedValue"));
            }
        });
    }
}
