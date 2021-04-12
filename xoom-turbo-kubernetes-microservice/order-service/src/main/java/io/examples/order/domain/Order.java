package io.examples.order.domain;

import io.examples.infrastructure.ApplicationRegistry;
import io.examples.infrastructure.messaging.MessagingClient;
import io.vlingo.actors.Address;
import io.vlingo.actors.Definition;
import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;

import java.util.UUID;

public interface Order {

    static String generateName() {
        return "O:" + UUID.randomUUID().toString();
    }

    static Completes<OrderState> register(final ApplicationRegistry registry,
                                          final ProductId productId,
                                          final Integer quantity,
                                          final Site site) {

        final Stage stage = registry.retrieveStage();

        final MessagingClient messagingClient = registry.retrieveMessagingClient();

        final Address address = stage.addressFactory().uniqueWith(generateName());

        final OrderId orderId = OrderId.from(address.idString());

        final Order order =
                stage.actorFor(Order.class,
                        Definition.has(OrderEntity.class, Definition.parameters(orderId)), address);

        final DomainEventNotifier notifier =
                stage.actorFor(DomainEventNotifier.class,
                        Definition.has(DomainEventNotifierActor.class, Definition.parameters(messagingClient)));

        return order.register(productId, quantity, site).andThen(newOrder -> {
            notifier.notify(new OrderWasRegistered(productId, quantity, site));
            return newOrder;
        });
    }

    Completes<OrderState> register(final ProductId productId, final Integer quantity, final Site site);

}
