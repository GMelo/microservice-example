package org.gmelo.hsbc.assignment.emitter.generator;

import org.gmelo.hsbc.assignment.emitter.model.TransactionalUser;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.gmelo.hsbc.assignment.emitter.model.TransactionalUser.aTransactionalUser;

/**
 * Created by gmelo on 14/08/18.
 */
@Service
public class TransactionNameGenerator {

    public final Random random = new Random(System.currentTimeMillis());

    /*
     * List of TransactionalUser created using the most common names in the U.S.
     */
    private final List<TransactionalUser> transactionalUserList = Arrays
            .asList(aTransactionalUser().withFirstName("Mary").withLastName("Smith").build(),
                    aTransactionalUser().withFirstName("Patricia").withLastName("Johnson").build(),
                    aTransactionalUser().withFirstName("Linda").withLastName("Williams").build(),
                    aTransactionalUser().withFirstName("Barbara").withLastName("Brown").build(),
                    aTransactionalUser().withFirstName("Elizabeth").withLastName("Jones").build(),
                    aTransactionalUser().withFirstName("James").withLastName("Miller").build(),
                    aTransactionalUser().withFirstName("John").withLastName("Davis").build(),
                    aTransactionalUser().withFirstName("Robert").withLastName("Garcia").build(),
                    aTransactionalUser().withFirstName("Michael").withLastName("Rodriguez").build(),
                    aTransactionalUser().withFirstName("William").withLastName("Wilson").build());

    /*
     * List of the most common Retailers in the U.S
     */
    private final List<String> retailerList = Arrays
            .asList("Wal-Mart", "The Kroger Co", "Costco", "The Home Depot", "Walgreen", "Target", "CVS", "Lowes", "Amazon.com", "Safeway");

    /**
     * Randomly returns a transactional user to be used for generation
     *
     * @return TransactionalUser user
     */
    public TransactionalUser getRandomTransactionUser() {
        return transactionalUserList.get(random.nextInt(10));
    }

    /**
     * Returns a Random retail name from the top 10 retailers in the U.S.
     *
     * @return
     */
    public String getRetailer() {
        return retailerList.get(random.nextInt(10));
    }

}
