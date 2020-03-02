package io.examples.account.domain.event;

import io.examples.account.domain.state.AccountStatus;
import io.vlingo.xoom.stepflow.Event;

public class AccountEvent extends Event {

    public AccountEvent(AccountStatus source, AccountStatus target) {
        super(source.name(), target.name());
    }

    public AccountEvent(String source, String target) {
        super(source, target);
    }
}
