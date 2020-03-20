// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.


package io.examples.calculation.domain;

import io.examples.infrastructure.CalculationQueries;
import io.vlingo.common.Completes;
import io.vlingo.lattice.query.StateObjectQueryActor;
import io.vlingo.symbio.store.MapQueryExpression;
import io.vlingo.symbio.store.QueryExpression;
import io.vlingo.symbio.store.object.ObjectStore;
import io.vlingo.symbio.store.object.ObjectStoreReader;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public class CalculationQueriesActor extends StateObjectQueryActor implements CalculationQueries {

    public CalculationQueriesActor(final ObjectStore objectStore) {
        super(objectStore);
    }

    @Override
    public Completes<Set<CalculationState>> allCalculations() {
        final QueryExpression queryExpression =
                MapQueryExpression.using(CalculationState.class, "findAll");

        return this.queryAll(Set.class, queryExpression, states -> (Set) states);
    }

}
