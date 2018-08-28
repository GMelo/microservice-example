package org.gmelo.hsbc.assignment.emitter.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * Created by gmelo on 15/08/18.
 * A model for the transaction, we use a different one to simulate the data
 * arriving from a external system.
 */
public class CreditCardTransaction {

    private final BigDecimal transactionValue;
    private final String vendor;
    private final String userName;
    private final String firstName;
    private final String lastName;
    private final Timestamp ts;

    private CreditCardTransaction(Builder builder) {
        this.transactionValue = builder.transactionValue;
        this.vendor = builder.vendor;
        this.userName = builder.transactionalUser.getUserName();
        this.firstName = builder.transactionalUser.getFirstName();
        this.lastName = builder.transactionalUser.getLastName();
        this.ts = new Timestamp(System.currentTimeMillis());
    }

    public BigDecimal getTransactionValue() {
        return transactionValue;
    }

    public String getVendor() {
        return vendor;
    }

    public String getUserName() {
        return userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Timestamp getTs() {
        return ts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CreditCardTransaction that = (CreditCardTransaction) o;

        if (!firstName.equals(that.firstName)) return false;
        if (!lastName.equals(that.lastName)) return false;
        if (!transactionValue.equals(that.transactionValue)) return false;
        if (!ts.equals(that.ts)) return false;
        if (!userName.equals(that.userName)) return false;
        if (!vendor.equals(that.vendor)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = transactionValue.hashCode();
        result = 31 * result + vendor.hashCode();
        result = 31 * result + userName.hashCode();
        result = 31 * result + firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        result = 31 * result + ts.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "CreditCardTransaction{" +
                "transactionValue=" + transactionValue +
                ", vendor='" + vendor + '\'' +
                ", userName='" + userName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", ts=" + ts +
                '}';
    }

    public static Builder aCreditCardTransaction() {
        return new Builder();
    }

    /**
     * {@code CreditCardTransaction} builder static inner class.
     */
    public static final class Builder {
        private BigDecimal transactionValue;
        private String vendor;
        private TransactionalUser transactionalUser;

        private Builder() {
        }

        /**
         * Sets the {@code transactionValue} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param transactionValue the {@code transactionValue} to set
         * @return a reference to this Builder
         */
        public Builder withTransactionValue(BigDecimal transactionValue) {
            this.transactionValue = transactionValue;
            return this;
        }

        /**
         * Sets the {@code vendor} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param vendor the {@code vendor} to set
         * @return a reference to this Builder
         */
        public Builder withVendor(String vendor) {
            this.vendor = vendor;
            return this;
        }

        /**
         * Sets the {@code transactionalUser} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param transactionalUser the {@code transactionalUser} to set
         * @return a reference to this Builder
         */
        public Builder withTransactionalUser(TransactionalUser transactionalUser) {
            this.transactionalUser = transactionalUser;
            return this;
        }

        /**
         * Returns a {@code CreditCardTransaction} built from the parameters previously set.
         *
         * @return a {@code CreditCardTransaction} built with parameters of this {@code CreditCardTransaction.Builder}
         */
        public CreditCardTransaction build() {
            validate();
            return new CreditCardTransaction(this);
        }

        public void validate() {
            Objects.requireNonNull(transactionValue, "transactionValue cannot be null");
            Objects.requireNonNull(vendor, "vendor cannot be null");
            Objects.requireNonNull(transactionalUser, "transactionalUser cannot be null");
        }
    }
}
