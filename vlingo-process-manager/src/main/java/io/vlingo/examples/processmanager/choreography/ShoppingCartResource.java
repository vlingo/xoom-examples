package io.vlingo.examples.processmanager.choreography;

import io.vlingo.actors.*;
import io.vlingo.common.Completes;
import io.vlingo.http.Response;
import io.vlingo.http.resource.Resource;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import static io.vlingo.common.serialization.JsonSerialization.serialized;
import static io.vlingo.http.Response.Status.Created;
import static io.vlingo.http.Response.Status.NotFound;
import static io.vlingo.http.Response.Status.Ok;
import static io.vlingo.http.ResponseHeader.*;
import static io.vlingo.http.resource.ResourceBuilder.*;
import static io.vlingo.http.resource.ResourceBuilder.get;

public class ShoppingCartResource {
    public static final String ROOT_URL = "/cart/";
    private final AddressFactory addressFactory;
    private final Stage stage;

    public ShoppingCartResource(final World world) {
        this.addressFactory = world.addressFactory();
        this.stage = world.stage();
    }

    public Completes<Response> create(final UserId userId) {
        // Check if already exists and then return 404?
        final Address entityAddress = addressFactory.uniquePrefixedWith("sc-");

        stage.actorFor(Definition.has(ShoppingCartEntity.class,
                Definition.parameters(userId)), ShoppingCart.class, entityAddress);

        return Completes.withSuccess(
                Response.of(Created,
                        headers(of(Location, location(userId.id))),
                        ""));
    }

    public Completes<Response> queryCart(String shoppingCartId) {
        return stage.actorOf(addressFactory.from(shoppingCartId), ShoppingCartEntity.class)
                .andThenInto(cart -> Completes.withSuccess(Response.of(Ok, serialized(cart.queryCart()))))
                .otherwise(noUser -> Response.of(NotFound, location(shoppingCartId)));
    }

    private String location(final String shoppingCartId) {
        return ROOT_URL + "/" + shoppingCartId;
    }

    public static class ShoppingCartChange {

        public ShoppingCartChange(String operation) {
            this.operation = operation;
        }
        public final String operation;

    }

    private Completes<Response> changeQuantity(Integer idOfUser, Integer idOfProduct, ShoppingCartChange shoppingCartChange) {
        throw new NotImplementedException();
    }

    public Resource routes() {

        return resource("ShoppingCart resource fluent api",
                post("/cart")
                        .body(UserId.class)
                        .handle(this::create),
                patch("/cart/{userId}/{productId}")
                        .param(Integer.class)
                        .param(Integer.class)
                        .param(ShoppingCartChange.class)
                        .handle(this::changeQuantity),
                get("/users/{userId}")
                        .param(String.class)
                        .handle(this::queryCart);
    }
}

