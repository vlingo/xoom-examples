// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.


package io.examples.infrastructure;

import io.examples.calculation.domain.CalculationQueriesActor;
import io.vlingo.actors.Stage;
import io.vlingo.symbio.store.state.StateStore;

public class CalculationQueryProvider {

    public final CalculationQueries queries;
    private static CalculationQueryProvider instance;

    public static CalculationQueryProvider using(final Stage stage, final StateStore stateStore) {
        if(instance == null) {
            instance = new CalculationQueryProvider(stage, stateStore);
        }
        return instance;
    }

    public static CalculationQueryProvider instance() {
        return instance;
    }

    private CalculationQueryProvider(final Stage stage, final StateStore stateStore) {
        this.queries = stage.actorFor(CalculationQueries.class, CalculationQueriesActor.class, stateStore);
    }

}
