package org.gmelo.hsbc.assignment.persister.repository.model;

import java.sql.Timestamp;

import org.bson.types.Decimal128;
import org.gmelo.hsbc.assignment.common.model.InternalTransactional;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by gmelo on 16/08/18.
 */

@Profile("transaction-persistence")
@Document(collection = "CreditCardTransaction")
public class TransactionDAO {

    @Indexed
    private String userName;
    @Indexed(direction = IndexDirection.DESCENDING)
    private Timestamp ts;
    //mongo does not handle big data very well
    private Decimal128 transactionValue;
    private String vendor;
    private String firstName;
    private String lastName;

    private TransactionDAO(InternalTransactional internalTransactional) {
        this.userName = internalTransactional.getUserName();
        this.ts = internalTransactional.getTs();
        this.transactionValue = new Decimal128(internalTransactional.getTransactionValue());
        this.vendor = internalTransactional.getVendor();
        this.firstName = internalTransactional.getFirstName();
        this.lastName = internalTransactional.getLastName();
    }

    public static TransactionDAO fromInternalTransactional(InternalTransactional internalTransactional) {
        return new TransactionDAO(internalTransactional);
    }


    public String getUserName() {
        return userName;
    }

    public Timestamp getTs() {
        return ts;
    }

    public Decimal128 getTransactionValue() {
        return transactionValue;
    }

    public String getVendor() {
        return vendor;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransactionDAO that = (TransactionDAO) o;

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
        int result = userName.hashCode();
        result = 31 * result + ts.hashCode();
        result = 31 * result + transactionValue.hashCode();
        result = 31 * result + vendor.hashCode();
        result = 31 * result + firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "TransactionDAO{" +
                "userName='" + userName + '\'' +
                ", ts=" + ts +
                ", transactionValue=" + transactionValue +
                ", vendor='" + vendor + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
