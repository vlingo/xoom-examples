package io.vlingo.pingpong.domain.impl;

import io.vlingo.actors.StatelessGridActor;
import io.vlingo.pingpong.domain.Pinger;
import io.vlingo.pingpong.domain.Ponger;

public class PingerActor extends StatelessGridActor implements Pinger {

  private Pinger self;

  public PingerActor() {
    self = selfAs(Pinger.class);
  }

  @Override
  public void ping(final Ponger ponger, String node) {
    System.out.printf("Pinger::ping::%s::%s%n", node, address());
    try {
      Thread.sleep(2000);
      ponger.pong(self, node);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
