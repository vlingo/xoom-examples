package io.vlingo.xoom.examples.eventjournal.counter;

import io.vlingo.xoom.common.Completes;

public interface CounterQuery {
    Completes<Integer> counter();
}
