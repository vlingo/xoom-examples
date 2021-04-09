package io.vlingo.xoom.examples.ecommerce.model;

import static io.vlingo.xoom.http.Response.Status.Created;
import static io.vlingo.xoom.http.Response.Status.NotFound;
import static io.vlingo.xoom.http.Response.Status.Ok;
import static io.vlingo.xoom.http.ResponseHeader.Location;
import static io.vlingo.xoom.http.ResponseHeader.headers;
import static io.vlingo.xoom.http.ResponseHeader.of;
import static io.vlingo.xoom.http.resource.ResourceBuilder.get;
import static io.vlingo.xoom.http.resource.ResourceBuilder.patch;
import static io.vlingo.xoom.http.resource.ResourceBuilder.post;
import static io.vlingo.xoom.http.resource.ResourceBuilder.resource;

import java.util.ArrayList;
import java.util.List;

import io.vlingo.xoom.actors.Address;
import io.vlingo.xoom.actors.AddressFactory;
import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.examples.ecommerce.model.Cart.CartItem;
import io.vlingo.xoom.http.Header.Headers;
import io.vlingo.xoom.http.ResponseHeader;
import io.vlingo.xoom.http.resource.ObjectResponse;
import io.vlingo.xoom.http.resource.Resource;

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

