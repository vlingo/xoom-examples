package io.vlingo.eventjournal.counter;

import io.vlingo.common.Completes;

public interface CounterQuery {
    Completes<Integer> counter();
}
