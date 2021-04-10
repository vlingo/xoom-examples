package io.vlingo.xoom.examples.reactive.messaging.patterns.contentbasedrouter.actor;

import io.vlingo.xoom.actors.Actor;
import io.vlingo.xoom.examples.reactive.messaging.patterns.contentbasedrouter.order.Order;

/**
 * @author Chandrabhan Kumhar
 * Handles inventory orders for InventorySystemX
 */
public class InventorySystemX extends Actor implements Inventory {

    @Override
    public void handleOrder(Order orderPlaced) {
        logger ().debug ( "Handling : " + orderPlaced );
    }
}
