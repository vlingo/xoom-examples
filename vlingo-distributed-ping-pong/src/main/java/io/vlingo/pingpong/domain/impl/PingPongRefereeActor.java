package io.vlingo.pingpong.domain.impl;

import io.vlingo.actors.Definition;
import io.vlingo.actors.StatelessGridActor;
import io.vlingo.common.Completes;
import io.vlingo.pingpong.domain.PingPongReferee;
import io.vlingo.pingpong.domain.Pinger;

public class PingPongRefereeActor extends StatelessGridActor implements PingPongReferee {

  @Override
  public Completes<Pinger> whistle(String name) {
    System.out.println(String.format("Whistling %s", name));
    final Pinger pinger = childActorFor(Pinger.class,
        Definition.has(PingerActor.class, Definition.NoParameters));
    return answerFrom(Completes.withSuccess(pinger));
  }
}
