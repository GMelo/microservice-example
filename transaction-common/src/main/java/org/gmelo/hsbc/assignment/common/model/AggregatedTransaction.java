package org.gmelo.hsbc.assignment.common.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * Created by gmelo on 18/08/18.
 * Internal representation of a AggregatedTransaction
 */
public class AggregatedTransaction implements Serializable {

    public static final String USER_NAME_FIELD_NAME = "userName";
    public static final String VALUE_FIELD_NAME = "aggregatedValue";

    private final String context;
    private final String userName;
    private final BigDecimal aggregatedValue;
    private final Timestamp to;
    private final Timestamp from;

    private AggregatedTransaction(Builder builder) {
        context = builder.context;
        userName = builder.userName;
        aggregatedValue = builder.value;
        to = builder.to;
        from = builder.from;
    }

    public String getContext() {
        return context;
    }

    public String getUserName() {
        return userName;
    }

    public BigDecimal getAggregatedValue() {
        return aggregatedValue;
    }

    public Timestamp getTo() {
        return to;
    }

    public Timestamp getFrom() {
        return from;
    }

    public static Builder aAggregatedTransaction() {
        return new Builder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AggregatedTransaction that = (AggregatedTransaction) o;

        if (!context.equals(that.context)) return false;
        if (!from.equals(that.from)) return false;
        if (!to.equals(that.to)) return false;
        if (!userName.equals(that.userName)) return false;
        if (!aggregatedValue.equals(that.aggregatedValue)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = context.hashCode();
        result = 31 * result + userName.hashCode();
        result = 31 * result + aggregatedValue.hashCode();
        result = 31 * result + to.hashCode();
        result = 31 * result + from.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "AggregatedTransaction{" +
                "context='" + context + '\'' +
                ", userName='" + userName + '\'' +
                ", aggregatedValue=" + aggregatedValue +
                ", to=" + to +
                ", from=" + from +
                '}';
    }

    /**
     * {@code AggregatedTransaction} builder static inner class.
     */
    public static final class Builder {
        private String context;
        private String userName;
        private BigDecimal value;
        private Timestamp to;
        private Timestamp from;

        private Builder() {
        }

        /**
         * Sets the {@code context} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param context the {@code context} to set
         * @return a reference to this Builder
         */
        public Builder withContext(String context) {
            this.context = context;
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
         * Sets the {@code aggregatedValue} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param value the {@code aggregatedValue} to set
         * @return a reference to this Builder
         */
        public Builder withAggregatedValue(BigDecimal value) {
            this.value = value;
            return this;
        }

        /**
         * Sets the {@code to} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param to the {@code to} to set
         * @return a reference to this Builder
         */
        public Builder withTo(Timestamp to) {
            this.to = to;
            return this;
        }

        /**
         * Sets the {@code from} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param from the {@code from} to set
         * @return a reference to this Builder
         */
        public Builder withFrom(Timestamp from) {
            this.from = from;
            return this;
        }

        /**
         * Returns a {@code AggregatedTransaction} built from the parameters previously set.
         *
         * @return a {@code AggregatedTransaction} built with parameters of this {@code AggregatedTransaction.Builder}
         */
        public AggregatedTransaction build() {
            validate();
            return new AggregatedTransaction(this);
        }

        public void validate() {
            Objects.requireNonNull(context, "context cannot be null");
            Objects.requireNonNull(userName, "userName cannot be null");
            Objects.requireNonNull(value, "aggregatedValue cannot be null");
            Objects.requireNonNull(to, "to cannot be null");
            Objects.requireNonNull(from, "from cannot be null");

        }
    }
}
