package io.examples.account.domain.state;

import io.examples.account.domain.Account;
import io.examples.account.flow.AccountFlow;
import io.vlingo.xoom.resource.annotations.Resource;
import io.vlingo.xoom.stepflow.StepFlow;
import io.vlingo.xoom.stepflow.State;
import io.vlingo.xoom.stepflow.Transition;
import io.vlingo.xoom.stepflow.TransitionHandler;

import static io.vlingo.xoom.stepflow.TransitionBuilder.from;
import static io.vlingo.xoom.stepflow.TransitionHandler.handle;

/**
 * The {@link AccountState} resource is a {@link State} implementation that defines event handlers
 * for transitioning between {@link AccountStatus} belonging to an {@link Account}. This class
 * will be automatically registered with a {@link StepFlow} in the Micronaut application context.
 *
 * @author Kenny Bastani
 * @see AccountFlow
 */
@Resource
public class AccountPending extends AccountState<AccountPending> {
    public static final AccountStatus TYPE = AccountStatus.ACCOUNT_PENDING;

    @Override
    public TransitionHandler[] getTransitionHandlers() {
        return new TransitionHandler[]{
                handle(from(this).to(new AccountConfirmed())
                        .then(Transition::logResult))
        };
    }

    @Override
    public String getName() {
        return TYPE.name();
    }
}
