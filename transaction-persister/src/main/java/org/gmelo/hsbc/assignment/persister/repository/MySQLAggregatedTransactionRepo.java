package org.gmelo.hsbc.assignment.persister.repository;

import org.gmelo.hsbc.assignment.persister.repository.model.AggregationDAO;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by gmelo on 16/08/18.
 */
@Profile("aggregation-persistence")
@Repository
public interface MySQLAggregatedTransactionRepo extends JpaRepository<AggregationDAO, Long> {
}
