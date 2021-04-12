package io.examples.infrastructure.messaging;

import io.examples.order.domain.DomainEvent;
import io.micronaut.configuration.rabbitmq.annotation.Binding;
import io.micronaut.configuration.rabbitmq.annotation.RabbitClient;

/**
 * The {@code MessagingClient} sends {@link io.examples.order.domain.DomainEvent}
 * through JMS channel / queue to other Bounded Contexts.
 *
 * @author Danilo Ambrosio
 */
@RabbitClient("order")
public interface MessagingClient {

    @Binding("registered-order")
    void publish(DomainEvent domainEvent);

}
