package io.examples.account.domain;

import io.examples.account.data.Identity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * The {@link Account} is the entity representation of an aggregate for a customer's account. This class is used
 * for both persistence and business logic that is exposed and controlled from the {@link AccountService}.
 *
 * @author Kenny Bastani
 */
@Entity
public class Account extends Identity {

    private String accountNumber;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<CreditCard> creditCards = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Address> addresses = new HashSet<>();

    public Account() {
    }

    /**
     * Instantiates a new {@link Account} entity.
     *
     * @param accountNumber is the customer's account number.
     * @param addresses     is a set of addresses for the customer's account.
     */
    public Account(String accountNumber, Set<Address> addresses) {
        this.accountNumber = accountNumber;
        this.addresses.addAll(addresses);
    }

    /**
     * Instantiates a new {@link Account} entity.
     *
     * @param accountNumber is the customer's account number.
     */
    public Account(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     * Get the account number for this customer's account.
     *
     * @return a unique account number for the customer's account.
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * Set the account number for this customer's account.
     *
     * @param accountNumber is a unique account number for the customer's account.
     */
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     * Get the collection of {@link CreditCard}s for this customer's account.
     *
     * @return a set of {@link CreditCard}s for this customer's account.
     */
    public Set<CreditCard> getCreditCards() {
        return creditCards;
    }

    /**
     * Set the collection of {@link CreditCard}s for this customer's account.
     *
     * @param creditCards is a set of {@link CreditCard}s for this customer's account.
     */
    public void setCreditCards(Set<CreditCard> creditCards) {
        this.creditCards = creditCards;
    }

    /**
     * Get a collection of {@link Address}es for this customer's account.
     *
     * @return a set of addresses for this customer's account.
     */
    public Set<Address> getAddresses() {
        return addresses;
    }

    /**
     * Set a collection of {@link Address}es for this customer's account.
     *
     * @param addresses is a set of addresses for this customer's account.
     */
    public void setAddresses(Set<Address> addresses) {
        this.addresses = addresses;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountNumber='" + accountNumber + '\'' +
                ", creditCards=" + creditCards +
                ", addresses=" + addresses +
                "} " + super.toString();
    }
}