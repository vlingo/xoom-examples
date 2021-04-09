// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.perf.vlingo.model.greeting;

import io.vlingo.common.Completes;
import io.vlingo.common.Tuple3;
import io.vlingo.lattice.model.stateful.StatefulEntity;
import io.vlingo.perf.vlingo.infrastructure.data.GreetingData;
import io.vlingo.perf.vlingo.infrastructure.data.UpdateGreetingData;
import io.vlingo.symbio.Source;

import java.util.Collections;
import java.util.List;

public final class GreetingEntity extends StatefulEntity<GreetingState> implements Greeting {
  private GreetingState state;

  public GreetingEntity() {
    super(); // uses GridAddress id as unique identity

    this.state = GreetingState.identifiedBy(id);
  }


  //=====================================
  // StatefulEntity


  @Override
  public Completes<GreetingState> defineGreeting(GreetingData data) {
    return apply(GreetingState.withGreetingData(id, data), () -> state);
  }


  @Override
  public Completes<GreetingState> updateMessage(UpdateGreetingData data) {
    return apply(state.updateMessage(data), () -> state);
  }

  @Override
  public Completes<GreetingState> updateDescription(UpdateGreetingData data) {
    return apply(state.updateDescription(data), () -> state);
  }

  //=====================================

  @Override
  protected void state(final GreetingState state) {
    this.state = state;
  }

  @Override
  protected Class<GreetingState> stateType() {
    return GreetingState.class;
  }

  @Override
  protected <C> Tuple3<GreetingState,List<Source<C>>,String> whenNewState() {
    if (state.isIdentifiedOnly()) {
      return null;
    }
    return Tuple3.from(state, Collections.emptyList(), "Greeting:new");
  }


}
