package io.vlingo.pingpong.domain;

public interface Pinger {
  void ping(final Ponger ponger, final String node);
}
