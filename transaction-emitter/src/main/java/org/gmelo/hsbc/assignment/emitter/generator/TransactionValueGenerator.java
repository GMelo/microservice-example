package org.gmelo.hsbc.assignment.emitter.generator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by gmelo on 14/08/18.
 * Created a monetary transaction within the bounds
 */
@Service
public class TransactionValueGenerator {

    @Value("${org.gmelo.hsbc.value.lower}")
    private  Integer lowerBound;
    @Value("${org.gmelo.hsbc.value.upper}")
    private Integer higherBound;

    public BigDecimal generateValue() {
        return new BigDecimal((Math.random() * (higherBound - lowerBound)) + lowerBound).setScale(2, BigDecimal.ROUND_UP);
    }

    public Integer getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(Integer lowerBound) {
        this.lowerBound = lowerBound;
    }

    public Integer getHigherBound() {
        return higherBound;
    }

    public void setHigherBound(Integer higherBound) {
        this.higherBound = higherBound;
    }
}

