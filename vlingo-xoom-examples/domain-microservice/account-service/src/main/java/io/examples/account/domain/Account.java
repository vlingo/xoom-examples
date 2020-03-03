package io.examples.account.domain;

import io.examples.account.data.Identity;
import io.examples.account.domain.event.AccountEvent;
import io.examples.account.domain.state.*;
import io.vlingo.xoom.stepflow.StepFlow;
import io.vlingo.xoom.stepflow.StateTransition;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import static io.examples.account.domain.state.AccountStatus.ACCOUNT_NUMBER_UPDATED;

/**
 * The {@link Account} is the entity representation of an aggregate for a customer's account. This class is used
 * for both persistence and business logic that is exposed and controlled from the {@link AccountService}.
 *
 * @author Kenny Bastani
 */
@Entity
public class Account extends Identity {

    private String accountNumber;
    private String version;
    private AccountStatus accountStatus;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<CreditCard> creditCards = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Address> addresses = new HashSet<>();

    public Account() {
        accountStatus = AccountStatus.ACCOUNT_CREATED;
    }

    /**
     * Instantiates a new {@link Account} entity.
     *
     * @param accountNumber is the customer's account number.
     * @param addresses     is a set of addresses for the customer's account.
     */
    public Account(String accountNumber, Set<Address> addresses) {
        this();
        this.accountNumber = accountNumber;
        this.addresses.addAll(addresses);
    }

    /**
     * Get the version of the {@link Account}, representing the last state change of the aggregate.
     *
     * @return the version of the entity's last state change
     */
    public String getVersion() {
        return version;
    }

    /**
     * Gets the logical state of the {@link Account}.
     *
     * @return the logical state of the {@link Account} as {@link AccountStatus}
     */
    public AccountStatus getAccountStatus() {
        return accountStatus;
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

    /**
     * Create a new {@link Account}. This command is triggered when a new {@link Account} is created
     * from the {@link AccountService}.
     *
     * @param processor is the process manager for this {@link Account}
     * @return the state of the {@link Account}
     */
    public Account create(StepFlow processor) {
        return sendEvent(processor, new AccountPending());
    }

    /**
     * Confirm the {@link Account}.
     *
     * @param processor is the process manager for this {@link Account}
     * @return the state of the {@link Account}
     */
    public Account confirm(StepFlow processor) {
        return sendEvent(processor, new AccountConfirmed());
    }

    /**
     * Activate the {@link Account}.
     *
     * @param processor is the process manager for this {@link Account}
     * @return the state of the {@link Account}
     */
    public Account activate(StepFlow processor) {
        return sendEvent(processor, new AccountActivated());
    }

    /**
     * Update the {@link Account}. An account can only be updated in an activated state.
     *
     * @param processor is the process manager for this {@link Account}
     * @return the state of the {@link Account}
     */
    @SuppressWarnings("unchecked")
    public Account update(StepFlow processor, Account model) {
        // Handle the sub-state transition for updating an active account's accountNumber
        AccountState targetState = new AccountActivated();

        // Generate the sub-state transition's address in a new event
        AccountEvent event = new AccountEvent(accountStatus.name(),
                targetState.getType() + "::" + ACCOUNT_NUMBER_UPDATED.name());

        // Send the event to the processor and update the account using the provided model
        return sendEvent(processor, event, stateTransition -> {
            stateTransition.apply(this);
            this.accountStatus = AccountStatus.valueOf(stateTransition.getTargetName());
            this.version = targetState.getVersion().toString();
            this.accountNumber = model.getAccountNumber();
        });
    }

    /**
     * Suspend the {@link Account}.
     *
     * @param processor is the process manager for this {@link Account}
     * @return the state of the {@link Account}
     */
    public Account suspend(StepFlow processor) {
        return sendEvent(processor, new AccountSuspended());
    }

    /**
     * Archive the {@link Account}.
     *
     * @param processor is the process manager for this {@link Account}
     * @return the state of the {@link Account}
     */
    public Account archive(StepFlow processor) {
        return sendEvent(processor, new AccountArchived());
    }

    /**
     * Send an {@link AccountEvent} to the process manager and mutate or reject the {@link AccountState}
     *
     * @param processor   is the process manager for this {@link Account}
     * @param targetState is the requested {@link AccountState} to attempt to transition to
     * @return the state of the {@link Account}
     */
    private Account sendEvent(StepFlow processor, AccountState targetState) {
        AccountEvent event = new AccountEvent(accountStatus, targetState.getType());
        return sendEvent(processor, event, defaultStateConsumer(targetState));
    }

    /**
     * Send an {@link AccountEvent} to the process manager and mutate or reject the {@link AccountState}
     *
     * @param processor is the process manager for this {@link Account}
     * @param event     is the {@link AccountEvent} describing the transition's address
     * @param handler   is the {@link Consumer} for mutating state if the new target state is accepted
     * @return the state of the {@link Account}
     */
    private Account sendEvent(StepFlow processor, AccountEvent event, Consumer<StateTransition> handler) {
        return Optional.ofNullable(processor.applyEvent(event)
                .andThenConsume(handler)
                .otherwise(transition -> null)
                .await()).map(transition -> this).orElseThrow(() -> new RuntimeException("The event with type ["
                + event.getEventType() +
                "] does not match a valid transition handler in the processor kernel."));
    }

    /**
     * A helper method for applying a default state transition without any additional business logic.
     * This consumer will mutate an accepted target state on the account by incrementing its version
     * and updating its {@link AccountStatus}.
     *
     * @param targetState is the requested {@link AccountState} to attempt to transition to
     * @return the default {@link Consumer}
     */
    @SuppressWarnings("unchecked")
    private Consumer<StateTransition> defaultStateConsumer(AccountState targetState) {
        return stateTransition -> {
            stateTransition.apply(this);
            this.accountStatus = AccountStatus.valueOf(stateTransition.getTargetName());
            this.version = targetState.getVersion().toString();
        };
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