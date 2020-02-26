package io.vlingo.examples.ecommerce.model;

import static io.vlingo.http.Response.Status.Created;
import static io.vlingo.http.Response.Status.NotFound;
import static io.vlingo.http.Response.Status.Ok;
import static io.vlingo.http.ResponseHeader.Location;
import static io.vlingo.http.ResponseHeader.headers;
import static io.vlingo.http.ResponseHeader.of;
import static io.vlingo.http.resource.ResourceBuilder.get;
import static io.vlingo.http.resource.ResourceBuilder.patch;
import static io.vlingo.http.resource.ResourceBuilder.post;
import static io.vlingo.http.resource.ResourceBuilder.resource;

import java.util.ArrayList;
import java.util.List;

import io.vlingo.actors.Address;
import io.vlingo.actors.AddressFactory;
import io.vlingo.actors.Definition;
import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;
import io.vlingo.examples.ecommerce.model.Cart.CartItem;
import io.vlingo.http.Header.Headers;
import io.vlingo.http.ResponseHeader;
import io.vlingo.http.resource.ObjectResponse;
import io.vlingo.http.resource.Resource;

public class CartResource {
    public static final String ROOT_URL = "/cart";

    private final AddressFactory addressFactory;
    private final Stage stage;
    @SuppressWarnings("unused")
    private final CartQuery cartQuery;

    public CartResource(final Stage stage, final AddressFactory addressFactory, CartQuery cartQuery) {
        this.addressFactory = addressFactory;
        this.stage = stage;
        this.cartQuery = cartQuery;
    }

    public Completes<ObjectResponse<String>> create(final UserId userId) {
        final Address cartAddress = addressFactory.uniquePrefixedWith("cart-");
        final Cart cartActor = stage.actorFor(
                Cart.class,
                Definition.has(CartActor.class, Definition.parameters(cartAddress.idString())),
                cartAddress);

        cartActor.createEmptyCartFor(userId);

        final Headers<ResponseHeader> headers = headers(of(Location, urlLocation(cartAddress.idString())));
        return Completes.withSuccess(ObjectResponse.of(Created, headers, ""));
    }

    public Completes<ObjectResponse<List<CartItem>>> queryCart(String cartId) {
        return stage.actorOf(Cart.class, addressFactory.from(cartId))
                .andThenTo(Cart::queryCart)
                .andThenTo(cartItems -> Completes.withSuccess(ObjectResponse.of(Ok, cartItems))
                .otherwise(noCart -> ObjectResponse.of(NotFound, new ArrayList<>(0))));
    }

    public Completes<ObjectResponse<List<CartItem>>> changeCartProductQuantity(String cartId, String idOfProduct, CartItemChange change) {
        return stage.actorOf(Cart.class, addressFactory.from(cartId))
                .andThenTo(cart -> change.applyTo(cart, ProductId.fromId(idOfProduct)))
                .andThenTo(cartItems -> Completes.withSuccess(ObjectResponse.of(Ok, cartItems)))
                .otherwise(noUser -> ObjectResponse.of(NotFound, new ArrayList<>(0)));
    }

    private String urlLocation(final String shoppingCartId) {
        return ROOT_URL + "/" + shoppingCartId;
    }

    public Resource<?> routes() {

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

    public static class CartItemChange {

        public final String operation;

        public CartItemChange(String operation) {
            this.operation = operation;
        }

        public Completes<List<CartItem>> applyTo(Cart cart, ProductId productId) {
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

