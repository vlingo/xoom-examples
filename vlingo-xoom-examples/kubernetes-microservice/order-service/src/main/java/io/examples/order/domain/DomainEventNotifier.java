package io.examples.order.domain;

import io.examples.infrastructure.messaging.MessagingClient;

import javax.inject.Singleton;

/**
 * {@code DomainEventNotifier} notifies whoever is interested in
 * a {@link DomainEvent}.
 *
 * @author Danilo Ambrosio
 */
@Singleton
public class DomainEventNotifier {

    private final MessagingClient messagingClient;

    public DomainEventNotifier(final MessagingClient messagingClient) {
        this.messagingClient = messagingClient;
    }

    public void notify(final DomainEvent domainEvent) {
        messagingClient.publish(domainEvent);
    }

}
