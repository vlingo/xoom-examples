package io.examples.account.domain.model;

import java.util.HashSet;
import java.util.Set;

/**
 * The {@link AccountQuery} is the entity representation of an aggregate for a customer's account.
 *
 * @author Kenny Bastani
 */
public class AccountQuery {

    private String accountNumber;
    private String version;
    private AccountStatus accountStatus;
    private Set<CreditCard> creditCards = new HashSet<>();
    private Set<AccountAddress> addresses = new HashSet<>();

    public AccountQuery() {
        accountStatus = AccountStatus.ACCOUNT_CREATED;
    }

    /**
     * Get the version of the {@link AccountQuery}, representing the last state change of the aggregate.
     *
     * @return the version of the entity's last state change
     */
    public String getVersion() {
        return version;
    }

    /**
     * Gets the logical state of the {@link AccountQuery}.
     *
     * @return the logical state of the {@link AccountQuery} as {@link AccountStatus}
     */
    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    /**
     * Instantiates a new {@link AccountQuery} entity.
     *
     * @param accountNumber is the customer's account number.
     */
    public AccountQuery(String accountNumber) {
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
     * Get a collection of {@link AccountAddress}es for this customer's account.
     *
     * @return a set of addresses for this customer's account.
     */
    public Set<AccountAddress> getAddresses() {
        return addresses;
    }

    /**
     * Set a collection of {@link AccountAddress}es for this customer's account.
     *
     * @param addresses is a set of addresses for this customer's account.
     */
    public void setAddresses(Set<AccountAddress> addresses) {
        this.addresses = addresses;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountNumber='" + accountNumber + '\'' +
                ", version='" + version + '\'' +
                ", accountStatus=" + accountStatus +
                ", creditCards=" + creditCards +
                ", addresses=" + addresses +
                "} " + super.toString();
    }
}