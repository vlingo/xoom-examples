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
    public static final String ROOT_URL = "/cart";
    private final AddressFactory addressFactory;
    private final Stage stage;

    public CartResource(final World world) {
        this.addressFactory = world.addressFactory();
        this.stage = world.stage();
    }

    public Completes<Response> create(final UserId userId) {
        final Address cartAddress = addressFactory.uniquePrefixedWith("cart-");
        stage.actorFor(
                Cart.class,
                Definition.has(CartEntity.class, Definition.parameters(cartAddress.idString(), userId)),
                cartAddress);
        return Completes.withSuccess(
                Response.of(Created,
                        headers(of(Location, urlLocation(cartAddress.idString()))), Body.from("{}")));
    }

    public Completes<Response> queryCart(String cartId) {
        return stage.actorOf(Cart.class, addressFactory.from(cartId))
                .andThenTo(Cart::queryCart)
                .andThenTo( cartItems -> Completes.withSuccess(Response.of(Ok, serialized(cartItems))))
                .otherwise( noCart -> Response.of(NotFound, urlLocation(cartId)));
    }

    public Completes<Response> changeCartProductQuantity(String cartId, String productId, CartItemChange change) {
        return stage.actorOf(Cart.class, addressFactory.from(cartId))
                .andThenTo(cart -> doChangeItem(cart, productId, change))
                .andThenTo(cartItems -> Completes.withSuccess(Response.of(Ok, serialized(cartItems))))
                .otherwise(noUser -> Response.of(NotFound, urlLocation(cartId)));
    }

    private Completes<List<Cart.CartItem>> doChangeItem(Cart entity, String idOfProduct, CartItemChange change) {
        ProductId productId = ProductId.fromId(idOfProduct);
        if (change.isAdd())
            return entity.addItem(productId);
        else
            return entity.removeItem(productId);
    }

    private String urlLocation(final String shoppingCartId) {
        return ROOT_URL + "/" + shoppingCartId;
    }


    public static class CartItemChange {

        public CartItemChange(String operation) {
            this.operation = operation;
        }

        public final String operation;

        public boolean isAdd() {
            return operation.equals("add");
        }
    }

    public Resource routes() {

        return resource("Cart resource fluent api",
                post("/cart")
                        .body(UserId.class)
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
}

