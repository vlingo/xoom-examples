package io.examples.order.domain.state;

import io.examples.order.domain.OrganizationStatus;
import io.vlingo.xoom.resource.annotations.Resource;
import io.vlingo.xoom.stepflow.State;
import io.vlingo.xoom.stepflow.Transition;
import io.vlingo.xoom.stepflow.TransitionHandler;

import static io.vlingo.xoom.stepflow.TransitionBuilder.from;
import static io.vlingo.xoom.stepflow.TransitionHandler.handle;

@Resource
public class Enabled extends State<Enabled> {

    @Override
    public String getName() {
        return OrganizationStatus.ENABLED.name();
    }

    @Override
    public TransitionHandler[] getTransitionHandlers() {
        return new TransitionHandler[]{
                handle(from(this).to(new Disabled()).then(Transition::logResult))
        };
    }
}
