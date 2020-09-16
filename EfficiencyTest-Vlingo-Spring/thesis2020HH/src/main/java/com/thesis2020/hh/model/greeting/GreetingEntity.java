package com.thesis2020.hh.model.greeting;


import io.vlingo.common.Completes;
import io.vlingo.common.Tuple3;
import io.vlingo.lattice.model.stateful.StatefulEntity;
import io.vlingo.symbio.Source;

import java.util.Collections;
import java.util.List;

import com.thesis2020.hh.infrastructure.data.GreetingData;
import com.thesis2020.hh.infrastructure.data.UpdateGreetingData;

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
      return apply(GreetingState.withGreetingData(id,data),() -> state);
  }


  @Override
  public Completes<GreetingState> updateMessage(UpdateGreetingData data) {
    return apply(state.updateMessage(data), ()-> state);
  }

  @Override
  public Completes<GreetingState> updateDescription(UpdateGreetingData data) {
    return apply(state.updateDescription(data),()->state);
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
