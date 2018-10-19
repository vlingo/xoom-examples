package io.vlingo.router.contentbased.actor;

import io.vlingo.router.contentbased.order.Order;
import io.vlingo.actors.Actor;

/**
 * @author Chandrabhan Kumhar
 * Handles inventory orders for InventorySystemX
 */
public class InventorySystemX extends Actor implements Inventory {

    @Override
    public void handleOrder(Order orderPlaced) {
        logger().log("Handling : " + orderPlaced);
    }
}
