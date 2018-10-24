package io.vlingo.reactive.messaging.patterns.contentbasedrouter;

import com.google.common.collect.Maps;
import io.vlingo.actors.Definition;
import io.vlingo.actors.World;
import io.vlingo.actors.testkit.TestUntil;
import io.vlingo.reactive.messaging.patterns.contentbasedrouter.actor.OrderRouter;
import io.vlingo.reactive.messaging.patterns.contentbasedrouter.actor.OrderRouterActor;
import io.vlingo.reactive.messaging.patterns.contentbasedrouter.order.Order;
import io.vlingo.reactive.messaging.patterns.contentbasedrouter.order.OrderItem;
import io.vlingo.reactive.messaging.patterns.contentbasedrouter.order.OrderPlaced;
import org.junit.Test;

import java.util.Map;

/**
 * @author Chandrabhan Kumhar
 * Test for Inventory and Order placed
 */
public class InventoryOrdersTest {

    private static final String WORLD_NAME = "store";

    /**
     * Test lifecycle of an {@link OrderPlaced}
     */
    @Test
    public void testOrderPlaced() {

        OrderItem orderItem1 = new OrderItem ( "1", "TypeABC.4", "An item of type ABC.4.", 29.95 );
        OrderItem orderItem2 = new OrderItem ( "2", "TypeABC.1", "An item of type ABC.1.", 99.95 );
        OrderItem orderItem3 = new OrderItem ( "3", "TypeABC.9", "An item of type ABC.9.", 14.95 );

        Map<String, OrderItem> items = Maps.newHashMap ();
        items.put ( orderItem1.getItemType (), orderItem1 );
        items.put ( orderItem2.getItemType (), orderItem2 );
        items.put ( orderItem3.getItemType (), orderItem3 );

        OrderPlaced orderPlaced = new OrderPlaced ( new Order ( "123", "TypeABC", items ) );

        OrderItem orderItem4 = new OrderItem ( "4", "TypeXYZ.2", "An item of type XYZ.2.", 74.95 );
        OrderItem orderItem5 = new OrderItem ( "5", "TypeXYZ.1", "An item of type XYZ.1.", 59.95 );
        OrderItem orderItem6 = new OrderItem ( "6", "TypeXYZ.7", "An item of type XYZ.7.", 29.95 );
        OrderItem orderItem7 = new OrderItem ( "7", "TypeXYZ.5", "An item of type XYZ.5.", 9.95 );

        Map<String, OrderItem> items1 = Maps.newHashMap ();
        items1.put ( orderItem4.getItemType (), orderItem4 );
        items1.put ( orderItem5.getItemType (), orderItem5 );
        items1.put ( orderItem6.getItemType (), orderItem6 );
        items1.put ( orderItem7.getItemType (), orderItem7 );

        OrderPlaced orderPlaced2 = new OrderPlaced ( new Order ( "124", "TypeXYZ", items1 ) );

        final World world = World.startWithDefaults ( WORLD_NAME );
        final TestUntil until = TestUntil.happenings ( 2 );

        final OrderRouter orderRouter = world.actorFor ( Definition.has ( OrderRouterActor.class, Definition.parameters ( until ) ), OrderRouter.class );
        orderRouter.routeOrder ( orderPlaced );
        orderRouter.routeOrder ( orderPlaced2 );

        until.completes ();
        world.terminate ();
    }
}
