package io.vlingo.reactive.messaging.patterns.contentbasedrouter.actor;

import io.vlingo.actors.Stoppable;
import io.vlingo.reactive.messaging.patterns.contentbasedrouter.order.OrderPlaced;

/**
 * @author Chandrabhan Kumhar
 * Dispatches order to specific Inventory for further processing
 */
public interface OrderRouter extends Stoppable {

    /**
     * Routes orders to specific inventory
     *
     * @param orderPlaced {@link OrderPlaced}
     */
    void routeOrder(final OrderPlaced orderPlaced);

}
