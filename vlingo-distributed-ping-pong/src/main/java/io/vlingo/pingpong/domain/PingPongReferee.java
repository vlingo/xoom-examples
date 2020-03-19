package io.vlingo.pingpong.domain;

import io.vlingo.common.Completes;

public interface PingPongReferee {
  Completes<Pinger> whistle(final String name);
}
