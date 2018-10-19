package io.vlingo.router.contentbased.actor;

import io.vlingo.router.contentbased.order.OrderPlaced;
import io.vlingo.actors.Stoppable;

/**
 * @author Chandrabhan Kumhar
 * Dispatches order to specific Inventory for further processing
 */
public interface OrderRouter extends Stoppable {

    /**
     * Routes orders to specific inventory
     * @param orderPlaced {@link OrderPlaced}
     */
    void routeOrder(final OrderPlaced orderPlaced);

}
