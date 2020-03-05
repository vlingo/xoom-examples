package io.examples.order.application;

import io.examples.order.domain.*;
import io.examples.order.repository.OrderRepository;
import io.vlingo.common.Completes;

import javax.inject.Singleton;

/**
 * The {@code OrderApplicationServices} exposes operations and business logic that
 * pertains to the {@link Order} domain model. This service forms an anti-corruption
 * layer that is exposed to consumers using the {@link io.examples.order.endpoint.v1.OrderResource}.
 *
 * @author Danilo Ambrosio
 * @see io.examples.order.endpoint.OrderEndpoint
 */
@Singleton
public class OrderApplicationServices {

    private final OrderRepository orderRepository;
    private final DomainEventNotifier domainEventNotifier;

    public OrderApplicationServices(final OrderRepository orderRepository, DomainEventNotifier domainEventNotifier) {
        this.orderRepository = orderRepository;
        this.domainEventNotifier = domainEventNotifier;
    }

    public Completes<Order> orderProduct(final RegisterOrder registerOrder) {
        final ProductId productId = ProductId.of(registerOrder.productId());
        final Site site = Site.valueOf(registerOrder.siteName());
        final Order order = Order.from(productId, registerOrder.quantity(), site);

        return Completes.withSuccess(orderRepository.save(order))
            .andThenConsume(newOrder -> domainEventNotifier.notify(
                    new OrderWasRegistered(newOrder.productId(), newOrder.quantity(), site)
            ));
    }

    public Completes<Iterable<Order>> allOrders() {
        return Completes.withSuccess(orderRepository.findAll());
    }

}
