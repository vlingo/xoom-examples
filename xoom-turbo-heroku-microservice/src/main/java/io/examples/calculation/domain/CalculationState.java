// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.examples.calculation.domain;

import io.vlingo.symbio.store.object.StateObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CalculationState extends StateObject {

    private final CalculationId id;
    private Integer result;
    private Operation operation;
    private final List<Integer> operands = new ArrayList<>();

    public static CalculationState from(final CalculationId calculationId) {
        return new CalculationState(calculationId);
    }

    private CalculationState(final CalculationId id) {
        this.id = id;
    }

    private CalculationState(final CalculationId id,
                             final Operation operation,
                             final Integer anOperand,
                             final Integer anotherOperand) {
        this(id);
        this.operation = operation;
        this.operands.addAll(Arrays.asList(anOperand, anotherOperand));
        this.result = operation.perform(this);
    }

    public CalculationState calculate(final Operation operation,
                          final Integer anOperand,
                          final Integer anotherOperand) {
        return new CalculationState(id, operation, anOperand, anotherOperand);
    }

    public boolean isApplicable(final Operation operation,
                                final Integer anOperand,
                                final Integer anotherOperand) {
        return this.operation.equals(operation) &&
                this.operands.containsAll(Arrays.asList(anOperand, anotherOperand));
    }

    public CalculationId id() {
        return id;
    }

    public Integer result() {
        return result;
    }

    protected Integer sum() {
        return this.operands.stream().reduce(0, Math::addExact);
    }

    protected Integer subtract() {
        return this.operands.stream().reduce(0, (a, b) -> Math.abs(a - b));
    }

    protected Integer multiply() {
        return this.operands.stream().reduce(1, Math::multiplyExact);
    }

}
