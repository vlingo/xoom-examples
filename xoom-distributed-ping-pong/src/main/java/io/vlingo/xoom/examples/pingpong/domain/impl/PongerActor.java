package io.vlingo.xoom.examples.pingpong.domain.impl;

import io.vlingo.xoom.actors.Actor;
import io.vlingo.xoom.actors.ActorProxyBase;
import io.vlingo.xoom.examples.pingpong.domain.Config;
import io.vlingo.xoom.examples.pingpong.domain.Mailer;
import io.vlingo.xoom.examples.pingpong.domain.Ponger;

public class PongerActor extends Actor implements Ponger {
  private final Ponger self;
  private final Mailer mailer;

  public PongerActor(final Mailer mailer) {
    this.self = selfAs(Ponger.class);
    this.mailer = mailer;
  }

  @Override
  public void pong(final String node) {
    logger().info("... from " + node);

    try {
      Thread.sleep(1_000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    mailer.send(self, Config.nodeName);
  }
}
