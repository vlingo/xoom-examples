package io.vlingo.pingpong.domain;

public interface Ponger {
  void pong(final Pinger pinger, final String node);
}
