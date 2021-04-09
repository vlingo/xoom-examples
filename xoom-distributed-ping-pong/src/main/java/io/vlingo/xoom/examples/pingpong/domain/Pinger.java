package io.vlingo.xoom.examples.pingpong.domain;

public interface Pinger {
  void ping(final Ponger ponger, final String node);
}
