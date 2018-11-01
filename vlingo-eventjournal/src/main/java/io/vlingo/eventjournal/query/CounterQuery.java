package io.vlingo.eventjournal.query;

import io.vlingo.common.Completes;

public interface CounterQuery {
    Completes<Integer> counter();
}
