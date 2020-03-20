package io.vlingo.pingpong.domain.impl;

import io.vlingo.actors.StatelessGridActor;
import io.vlingo.pingpong.domain.Pinger;
import io.vlingo.pingpong.domain.Ponger;

public class PongerActor extends StatelessGridActor implements Ponger {

  private final Ponger self;

  public PongerActor() {
    self = selfAs(Ponger.class);
  }

  @Override
  public void pong(final Pinger pinger, final String node) {
    System.out.printf("Ponger::pong::%s::%s%n", node, address());
    try {
      Thread.sleep(2000);
      pinger.ping(self, node);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
