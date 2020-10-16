package io.vlingo.pingpong.domain.impl;

import io.vlingo.actors.Actor;
import io.vlingo.actors.ActorProxyBase;
import io.vlingo.pingpong.domain.Config;
import io.vlingo.pingpong.domain.Mailer;
import io.vlingo.pingpong.domain.Pinger;
import io.vlingo.pingpong.domain.Ponger;

public class MailerActor extends Actor implements Mailer {
	private final Pinger pinger;

	public MailerActor(Pinger pinger) {
		this.pinger = ActorProxyBase.thunk(stage(), pinger); // for 'distributed' actor and mailbox setup
	}

	@Override
	public void send(final Ponger ponger, final String node) {
		logger().info("... from " + node);

		try {
			Thread.sleep(1_000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		pinger.ping(ponger, Config.nodeName);
	}
}
