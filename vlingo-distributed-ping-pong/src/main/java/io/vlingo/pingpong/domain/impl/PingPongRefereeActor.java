package io.vlingo.pingpong.domain.impl;

import io.vlingo.actors.Actor;
import io.vlingo.actors.Definition;
import io.vlingo.common.Completes;
import io.vlingo.pingpong.domain.PingPongReferee;
import io.vlingo.pingpong.domain.Pinger;

public class PingPongRefereeActor extends Actor implements PingPongReferee {

  @Override
  public Completes<Pinger> whistle(String name) {
    System.out.println(String.format("Referee %s whistling start from %s", address(), name));
    final Pinger pinger;
    try {
      pinger = childActorFor(Pinger.class,
          Definition.has(PingerActor.class, Definition.NoParameters));
    }
    catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
    return answerFrom(Completes.withSuccess(pinger));
  }
}
