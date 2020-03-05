package io.examples.account.flow;

import io.examples.account.domain.state.AccountState;
import io.vlingo.common.Completes;
import io.vlingo.xoom.stepflow.Kernel;
import io.vlingo.xoom.stepflow.StepFlow;
import io.vlingo.xoom.stepflow.FlowActor;
import io.vlingo.xoom.stepflow.State;

import java.util.List;

/**
 * The {@link AccountFlow} creates a {@link FlowActor} that compiles a {@link Kernel} for the list of
 * provided {@link AccountState<State>} implementations. All state transitions are driven through fluent models
 * that do not delegate control to a configuration class or DSL. The {@link StepFlow} understands how to stitch
 * together the graph of transitions by making API calls to its attached {@link Kernel} implementation.
 *
 * @author Kenny Bastani
 */
public class AccountFlow extends FlowActor {

    public AccountFlow(List<State> states) {
        super(states);
    }

    @Override
    public Completes<String> getName() {
        return completes().with("Account Flow");
    }
}
