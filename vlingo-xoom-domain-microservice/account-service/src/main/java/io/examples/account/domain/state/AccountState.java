package io.examples.account.domain.state;


import io.examples.account.domain.Account;
import io.examples.account.flow.AccountFlow;
import io.vlingo.xoom.stepflow.StepFlow;
import io.vlingo.xoom.stepflow.State;

/**
 * The {@link AccountState} resource is a {@link State} implementation that defines event handlers
 * for transitioning between {@link AccountStatus} belonging to an {@link Account}. This class
 * will be automatically registered with a {@link StepFlow} in the Micronaut application context.
 *
 * @author Kenny Bastani
 * @see AccountFlow
 */
public abstract class AccountState<T extends State> extends State<T> {

    public AccountState() {
    }

    public AccountStatus getType() {
        return AccountStatus.valueOf(getName());
    }

    @Override
    public String toString() {
        return "AccountState{} " + super.toString();
    }

}
