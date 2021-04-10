package io.vlingo.xoom.examples.reactive.messaging.patterns.contentbasedrouter.actor;

import io.vlingo.xoom.actors.Stoppable;
import io.vlingo.xoom.examples.reactive.messaging.patterns.contentbasedrouter.order.Order;

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
