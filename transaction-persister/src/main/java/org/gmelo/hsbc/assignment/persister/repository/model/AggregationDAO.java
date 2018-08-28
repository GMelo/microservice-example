package org.gmelo.hsbc.assignment.persister.repository.model;


import org.gmelo.hsbc.assignment.common.model.AggregatedTransaction;
import org.springframework.context.annotation.Profile;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by gmelo on 18/08/18.
 */
@Profile("aggregation-persistence")
@Entity
@Table(name = "aggregated_transactions")
public class AggregationDAO implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long db_id;
    private String context;
    private String userName;
    private BigDecimal aggregatedValue;
    private Timestamp query_to;
    private Timestamp query_from;

    private AggregationDAO(AggregatedTransaction aggregatedTransaction) {
        this.context = aggregatedTransaction.getContext();
        this.userName = aggregatedTransaction.getUserName();
        this.aggregatedValue = aggregatedTransaction.getAggregatedValue();
        this.query_to = aggregatedTransaction.getTo();
        this.query_from = aggregatedTransaction.getFrom();
    }

    public AggregationDAO() {
    }

    public static AggregationDAO fromAggregatedTransaction(AggregatedTransaction aggregatedTransaction) {
        return new AggregationDAO(aggregatedTransaction);
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getDb_id() {
        return db_id;
    }

    public void setDb_id(Long db_id) {
        this.db_id = db_id;
    }

    public BigDecimal getAggregatedValue() {
        return aggregatedValue;
    }

    public void setAggregatedValue(BigDecimal aggregatedValue) {
        this.aggregatedValue = aggregatedValue;
    }

    public Timestamp getQuery_to() {
        return query_to;
    }

    public void setQuery_to(Timestamp query_to) {
        this.query_to = query_to;
    }

    public Timestamp getQuery_from() {
        return query_from;
    }

    public void setQuery_from(Timestamp query_from) {
        this.query_from = query_from;
    }

    @Override
    public String toString() {
        return "AggregationDAO{" +
                "db_id=" + db_id +
                ", context='" + context + '\'' +
                ", userName='" + userName + '\'' +
                ", aggregatedValue=" + aggregatedValue +
                ", query_to=" + query_to +
                ", query_from=" + query_from +
                '}';
    }
}
