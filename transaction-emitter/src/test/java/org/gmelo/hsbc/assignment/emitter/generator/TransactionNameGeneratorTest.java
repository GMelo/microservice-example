package org.gmelo.hsbc.assignment.emitter.generator;

import org.gmelo.hsbc.assignment.emitter.model.TransactionalUser;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class TransactionNameGeneratorTest {

    private TransactionNameGenerator transactionNameGenerator = new TransactionNameGenerator();

    @Test
    public void testGetRandomTransactionUser() throws Exception {
        List<TransactionalUser> transactionalUserList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            transactionalUserList.add(transactionNameGenerator.getRandomTransactionUser());
        }
        Map<String, Long> usernameCount = transactionalUserList.stream().collect(Collectors.groupingBy(user -> user.getUserName(), Collectors.counting()));

        assertEquals((Long) 1000L, usernameCount.values().stream().reduce(Long::sum).get());

        usernameCount
                .values()
                .forEach(count -> assertTrue(count > 50));
    }
}