package io.vlingo.perf.vlingo.infrastructure.persistence;

import io.vlingo.common.Completes;
import io.vlingo.perf.vlingo.model.greeting.GreetingState;

import java.util.Collection;

public interface Queries {
    Completes<GreetingState> greetingWithId(final String id);
    Completes<Collection<GreetingState>> greetings();
}
