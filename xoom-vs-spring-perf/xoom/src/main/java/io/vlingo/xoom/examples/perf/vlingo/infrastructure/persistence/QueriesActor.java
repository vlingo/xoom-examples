// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.perf.vlingo.infrastructure.persistence;

import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.lattice.query.StateStoreQueryActor;
import io.vlingo.xoom.examples.perf.vlingo.model.greeting.GreetingState;
import io.vlingo.xoom.symbio.store.state.StateStore;

import java.util.ArrayList;
import java.util.Collection;

public class QueriesActor extends StateStoreQueryActor implements Queries {

    public QueriesActor(final StateStore stateStore){
        super(stateStore);
    }

    @Override
    public Completes<GreetingState> greetingWithId(String id) {
        return queryStateFor(id,GreetingState.class, GreetingState.empty());
    }

    @Override
    public Completes<Collection<GreetingState>> greetings() {
        return streamAllOf(GreetingState.class,new ArrayList<>());
    }
}
