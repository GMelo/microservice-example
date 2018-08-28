package org.gmelo.hsbc.assignment.common.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by gmelo on 16/08/18.
 * Internal representation of InternalTransactional
 */
public class InternalTransactional implements Serializable {

    public static final String TRANSACTIONAL_VALUE_FIELD_NAME = "transactionValue";
    public static final String VENDOR_FIELD_NAME = "vendor";
    public static final String USER_NAME_FIELD_NAME = "userName";
    public static final String FIRST_NAME_FIELD_NAME = "firstName";
    public static final String LAST_NAME_FIELD_NAME = "lastName";
    public static final String TS_FIELD_NAME = "ts";

    private final BigDecimal transactionValue;
    private final String vendor;
    private final String userName;
    private final String firstName;
    private final String lastName;
    private final Timestamp ts;

    private InternalTransactional(Builder builder) {
        this.transactionValue = builder.transactionValue;
        this.vendor = builder.vendor;
        this.userName = builder.userName;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.ts = builder.ts;
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

    public static Builder aInternalTransactionalTransaction() {
        return new Builder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InternalTransactional that = (InternalTransactional) o;

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
        return "InternalTransactionalTransaction{" +
                "transactionValue=" + transactionValue +
                ", vendor='" + vendor + '\'' +
                ", userName='" + userName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", ts=" + ts +
                '}';
    }

    /**
     * {@code InternalTransactionalTransaction} builder static inner class.
     */
    public static final class Builder {
        private BigDecimal transactionValue;
        private String vendor;
        private String userName;
        private String firstName;
        private String lastName;
        private Timestamp ts;

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
         * Sets the {@code userName} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param userName the {@code userName} to set
         * @return a reference to this Builder
         */
        public Builder withUserName(String userName) {
            this.userName = userName;
            return this;
        }

        /**
         * Sets the {@code firstName} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param firstName the {@code firstName} to set
         * @return a reference to this Builder
         */
        public Builder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        /**
         * Sets the {@code lastName} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param lastName the {@code lastName} to set
         * @return a reference to this Builder
         */
        public Builder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        /**
         * Sets the {@code ts} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param ts the {@code ts} to set
         * @return a reference to this Builder
         */
        public Builder withTs(Timestamp ts) {
            this.ts = ts;
            return this;
        }

        /**
         * Returns a {@code InternalTransactionalTransaction} built from the parameters previously set.
         *
         * @return a {@code InternalTransactionalTransaction} built with parameters of this {@code InternalTransactionalTransaction.Builder}
         */
        public InternalTransactional build() {
            return new InternalTransactional(this);
        }
    }
}
