package io.vlingo.reactive.messaging.patterns.contentbasedrouter.actor;

import io.vlingo.actors.Stoppable;
import io.vlingo.reactive.messaging.patterns.contentbasedrouter.order.Order;

/**
 * @author Chandrabhan Kumhar
 */
public interface Inventory extends Stoppable {

    /**
     * Handles order for inventory
     *
     * @param order {@link Order}
     */
    void handleOrder(Order order);

}
