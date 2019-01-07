package io.vlingo.reactive.messaging.patterns.contentbasedrouter.actor;

import io.vlingo.actors.Actor;
import io.vlingo.actors.World;
import io.vlingo.actors.testkit.TestUntil;
import io.vlingo.reactive.messaging.patterns.contentbasedrouter.order.OrderPlaced;

/**
 * @author Chandrabhan Kumhar
 * Filter and routes orders to inventory on the criteria specified
 */
public class OrderRouterActor extends Actor implements OrderRouter {

    private static final String TYPE_ABC = "TypeABC";
    private static final String TYPE_XYZ = "TypeXYZ";

    private Inventory inventorySystemA;
    private Inventory inventorySystemX;
    private TestUntil testUntil;

    public OrderRouterActor(final TestUntil testUntil) {
        World world = stage ().world ();
        this.testUntil = testUntil;
        inventorySystemA = world.actorFor ( Inventory.class, InventorySystemA.class );
        inventorySystemX = world.actorFor ( Inventory.class, InventorySystemX.class );
    }

    /**
     * Routes order on the basis of type of order specified
     *
     * @param orderPlaced {@link OrderPlaced}
     */
    @Override
    public void routeOrder(final OrderPlaced orderPlaced) {

        if (orderPlaced.getOrder ().getType ().contains ( TYPE_ABC )) {
            inventorySystemA.handleOrder ( orderPlaced.getOrder () );
        } else if (orderPlaced.getOrder ().getType ().contains ( TYPE_XYZ )) {
            inventorySystemX.handleOrder ( orderPlaced.getOrder () );
        } else {
            logger ().log ( "OrderRouter: received unexpected message" );
        }
        this.testUntil.happened ();
    }

    @Override
    protected void afterStop() {
        this.testUntil.happened ();
        super.afterStop ();
    }
}
