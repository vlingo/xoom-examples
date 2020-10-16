package io.vlingo.pingpong.domain;

public interface Mailer {
	void send(final Ponger ponger, final String node);
}
