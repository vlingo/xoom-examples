package io.examples.order.infra.repository;

import io.examples.order.domain.OrderShippingAddress;
import io.examples.order.domain.Order;
import io.examples.order.domain.state.OrderStatus;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {

    void update(@Id Long id, OrderStatus status, String version);

    void updateShippingAddress(@Id Long id, OrderShippingAddress shippingAddress);
}
