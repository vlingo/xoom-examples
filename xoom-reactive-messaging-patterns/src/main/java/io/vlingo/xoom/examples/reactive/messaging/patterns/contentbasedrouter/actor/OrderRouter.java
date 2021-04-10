package io.vlingo.xoom.examples.reactive.messaging.patterns.contentbasedrouter.actor;

import io.vlingo.xoom.actors.Stoppable;
import io.vlingo.xoom.examples.reactive.messaging.patterns.contentbasedrouter.order.OrderPlaced;

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
