package io.examples.order.domain;

import io.examples.infrastructure.messaging.MessagingClient;
import io.vlingo.actors.Actor;
import io.vlingo.common.Completes;

public class DomainEventNotifierActor extends Actor implements DomainEventNotifier {

    private final MessagingClient messagingClient;

    public DomainEventNotifierActor(final MessagingClient messagingClient) {
        this.messagingClient = messagingClient;
    }

    @Override
    public void notify(final DomainEvent domainEvent) {
        Completes.withSuccess(messagingClient).andThenConsume(client -> client.publish(domainEvent));
    }
}
