package io.examples.order.repository;

import io.examples.order.domain.Order;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

/**
 * {@code OrderRepository} is capable to persist and retrieve
 * {@link Order} data.
 *
 * @author Danilo Ambrosio
 */
@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {

}
