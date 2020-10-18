package io.vlingo.pingpong.domain.impl;

import io.vlingo.actors.Actor;
import io.vlingo.actors.ActorProxyBase;
import io.vlingo.pingpong.domain.Config;
import io.vlingo.pingpong.domain.Mailer;
import io.vlingo.pingpong.domain.Ponger;

public class PongerActor extends Actor implements Ponger {
  private final Ponger self;
  private final Mailer mailer;

  public PongerActor(final Mailer mailer) {
    this.self = selfAs(Ponger.class);
    this.mailer = ActorProxyBase.thunk(stage(), mailer); // for 'distributed' actor and mailbox setup
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
