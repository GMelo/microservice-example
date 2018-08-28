package org.gmelo.hsbc.assignment.emitter.model;

import java.util.Objects;

/**
 * Created by gmelo on 14/08/18.
 */
public class TransactionalUser {

    private final String firstName;
    private final String lastName;
    private final String userName;

    private TransactionalUser(Builder builder) {
        firstName = builder.firstName;
        lastName = builder.lastName;
        userName = builder.userName;
    }

    public static Builder aTransactionalUser() {
        return new Builder();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserName() {
        return userName;
    }

    /**
     * {@code TransactionalUser} builder static inner class.
     */
    public static final class Builder {
        private String firstName;
        private String lastName;
        private String userName;

        private Builder() {
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
         * Returns a {@code TransactionalUser} built from the parameters previously set.
         *
         * @return a {@code TransactionalUser} built with parameters of this {@code TransactionalUser.Builder}
         */
        public TransactionalUser build() {
            validate();
            this.userName = lastName + firstName.charAt(0);
            return new TransactionalUser(this);
        }

        /*
         * In the real world we would also ensure the string was not empty ""
         */
        public void validate() {
            Objects.requireNonNull(lastName, "lastName Cannot be null");
            Objects.requireNonNull(firstName, "firstName Cannot be null");
        }


    }
}
