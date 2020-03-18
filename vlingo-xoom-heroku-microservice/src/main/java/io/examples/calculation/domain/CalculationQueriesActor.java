// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.


package io.examples.calculation.domain;

import io.examples.infrastructure.CalculationQueries;
import io.vlingo.actors.Actor;
import io.vlingo.common.Completes;
import io.vlingo.reactivestreams.Sink;
import io.vlingo.reactivestreams.Stream;
import io.vlingo.symbio.Entry;
import io.vlingo.symbio.store.state.StateStore;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class CalculationQueriesActor extends Actor implements CalculationQueries {

    private final StateStore stateStore;

    public CalculationQueriesActor(final StateStore stateStore) {
        this.stateStore = stateStore;
    }

    @Override
    public Completes<List<CalculationState>> allCalculations() {
        final List<CalculationState> results = new ArrayList<>();

        final Sink<CalculationState> sink =
                Sink.consumeWith(state -> results.add(state));

        return stateStore.entryReader("states")
                .andFinallyConsume(reader -> {
                    reader.size().andThenConsume(size -> {
                        IntStream.range(0, size.intValue()).forEach(index -> {

                        });
                    });
                })
                .andThenTo(stream -> Completes.withSuccess(results))
                .andFinally();
    }

    @Override
    public Completes<Optional<CalculationState>> calculationOf(final Operation operation,
                                                               final Integer anOperand,
                                                               final Integer anotherOperand) {
        final Predicate<CalculationState> applicabilityFilter =
                state -> state.isApplicable(operation, anOperand, anotherOperand);

        return allCalculations().andThenTo(calculations -> {
            return Completes.withFailure(calculations.stream().filter(applicabilityFilter).findFirst());
        });
    }
}
