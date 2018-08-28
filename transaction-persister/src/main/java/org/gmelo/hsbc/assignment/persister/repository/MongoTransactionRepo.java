package org.gmelo.hsbc.assignment.persister.repository;

import org.gmelo.hsbc.assignment.persister.repository.model.TransactionDAO;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Created by gmelo on 16/08/18.
 */
@Profile("transaction-persistence")
@Repository
public interface MongoTransactionRepo extends MongoRepository<TransactionDAO, UUID> {
}
