package io.vlingo.examples.ecommerce.model;

import io.vlingo.actors.*;
import io.vlingo.common.Completes;
import io.vlingo.http.Body;
import io.vlingo.http.Response;
import io.vlingo.http.resource.Resource;

import java.util.List;

import static io.vlingo.common.serialization.JsonSerialization.serialized;
import static io.vlingo.http.Response.Status.*;
import static io.vlingo.http.ResponseHeader.*;
import static io.vlingo.http.resource.ResourceBuilder.*;

public class CartResource {
    public static final String         ROOT_URL = "/cart";
    private final       AddressFactory addressFactory;
    private final       Stage          stage;

    public CartResource(final World world) {
        this.addressFactory = world.addressFactory();
        this.stage = world.stage();
    }

    public Completes<Response> create(final UserId userId) {
        final Address cartAddress = addressFactory.uniquePrefixedWith("cart-");
        Cart cartActor = stage.actorFor(
                Cart.class,
                Definition.has(CartEntity.class, Definition.parameters(cartAddress.idString())),
                cartAddress);

        cartActor.createEmptyCartFor(userId);

        return Completes.withSuccess(
                Response.of(Created,
                        headers(of(Location, urlLocation(cartAddress.idString()))),
                        Body.from("")));
    }

    public Completes<Response> queryCart(String cartId) {
        return stage.actorOf(Cart.class, addressFactory.from(cartId))
                .andThenTo(Cart::queryCart)
                .andThenTo(cartItems -> Completes.withSuccess(Response.of(Ok, serialized(cartItems))))
                .otherwise(noCart -> Response.of(NotFound, urlLocation(cartId)));
    }

    public Completes<Response> changeCartProductQuantity(String cartId, String idOfProduct, CartItemChange change) {
        return stage.actorOf(Cart.class, addressFactory.from(cartId))
                .andThenTo(cart -> change.applyTo(cart, ProductId.fromId(idOfProduct)))
                .andThenTo(cartItems -> Completes.withSuccess(Response.of(Ok, serialized(cartItems))))
                .otherwise(noUser -> Response.of(NotFound, urlLocation(cartId)));
    }

    private String urlLocation(final String shoppingCartId) {
        return ROOT_URL + "/" + shoppingCartId;
    }

    public Resource<?> routes() {

        return resource("Cart resource fluent api",
                post("/cart")
                        .body(UserId.class, new GsonMapper())
                        .handle(this::create),
                patch("/cart/{cartId}/{productId}")
                        .param(String.class)
                        .param(String.class)
                        .body(CartItemChange.class)
                        .handle(this::changeCartProductQuantity),
                get("/cart/{cartId}")
                        .param(String.class)
                        .handle(this::queryCart));
    }

    public static class CartItemChange {

        public final String operation;

        public CartItemChange(String operation) {
            this.operation = operation;
        }

        public Completes<List<Cart.CartItem>> applyTo(Cart cart, ProductId productId) {
            if (operation.equals("add")) {
                return cart.addItem(productId);
            } else if (operation.equals("remove")) {
                return cart.removeItem(productId);
            } else {
                throw new IllegalArgumentException("Operation invalid: " + operation);
            }
        }

    }
}

