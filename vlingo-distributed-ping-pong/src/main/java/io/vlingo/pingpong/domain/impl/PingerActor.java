package io.vlingo.pingpong.domain.impl;

import io.vlingo.actors.Actor;
import io.vlingo.pingpong.domain.Config;
import io.vlingo.pingpong.domain.Pinger;
import io.vlingo.pingpong.domain.Ponger;

public class PingerActor extends Actor implements Pinger {

  @Override
  public void ping(final Ponger ponger, final String node) {
    logger().info("... from " + node);

    try {
      Thread.sleep(1_000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ponger.pong(Config.nodeName);
  }
}
