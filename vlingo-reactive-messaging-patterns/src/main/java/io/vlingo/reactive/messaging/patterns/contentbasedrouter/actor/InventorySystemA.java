package io.vlingo.reactive.messaging.patterns.contentbasedrouter.actor;

import io.vlingo.actors.Actor;
import io.vlingo.reactive.messaging.patterns.contentbasedrouter.order.Order;

/**
 * @author Chandrabhan Kumhar
 * Handles inventory orders for InventorySystemA
 */
public class InventorySystemA extends Actor implements Inventory {

    @Override
    public void handleOrder(Order orderPlaced) {
        logger ().log ( "Handling : " + orderPlaced );
    }

}
