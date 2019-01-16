package io.vlingo.examples.ecommerce.model;

import io.vlingo.actors.*;
import io.vlingo.common.Completes;
import io.vlingo.http.Body;
import io.vlingo.http.Response;
import io.vlingo.http.resource.Resource;

import java.util.HashMap;
import java.util.Map;

import static io.vlingo.common.serialization.JsonSerialization.serialized;
import static io.vlingo.http.Response.Status.Created;
import static io.vlingo.http.Response.Status.NotFound;
import static io.vlingo.http.Response.Status.Ok;
import static io.vlingo.http.ResponseHeader.*;
import static io.vlingo.http.resource.ResourceBuilder.*;

public class OrderResource {
    private static final String         ROOT_URL = "/order";
    private final        AddressFactory addressFactory;
    private final        Stage          stage;

    public OrderResource(final World world) {
        this.addressFactory = world.addressFactory();
        this.stage = world.stage();
    }

    private Completes<Response> create(final OrderCreateRequest request) {
        final Address orderAddress = addressFactory.uniquePrefixedWith("order-");
        stage.actorFor(
                Cart.class,
                Definition.has(CartEntity.class,
                        Definition.parameters(orderAddress.idString(), request.userId, request.quantityByProductId)),
                orderAddress);
        return Completes.withSuccess(
                Response.of(Created,
                        headers(of(Location, urlLocation(orderAddress.idString()))), Body.from("{}")));
    }


    private Completes<Response> queryOrder(String orderId) {
        return stage.actorOf(Order.class, addressFactory.from(orderId))
                .andThenTo(Order::query)
                .andThenTo( orderInfo -> Completes.withSuccess(Response.of(Ok, serialized(orderInfo))))
                .otherwise( noOrder -> Response.of(NotFound, urlLocation(orderId)));
    }

    private String urlLocation(final String orderId) {
        return ROOT_URL + "/" + orderId;
    }

    public static class OrderCreateRequest {
        public OrderCreateRequest(Map<ProductId, Integer> quantityByProductId, UserId userId) {
            this.quantityByProductId = quantityByProductId;
            this.userId = userId;
        }
        public final Map<ProductId, Integer> quantityByProductId;
        public final UserId userId;

        public static class Builder {
            private Map<ProductId, Integer>  quantityByProductId;
            private UserId userId;

            private Builder() {
                quantityByProductId = new HashMap<>();
            }
            public static Builder create() {
                return new Builder();
            }
            public Builder withProduct(ProductId productId, Integer quantity) {
                quantityByProductId.put(productId, quantity);
                return this;
            }
            public Builder withUser(UserId userId) {
                this.userId = userId;
                return this;
            }
            public OrderCreateRequest build() {
                return new OrderCreateRequest(quantityByProductId, userId);
            }
        }
    }

    public Resource routes() {
        //todo: fix issue where resources can overwrite each other even though they are not equal
        return resource("Order resource fluent api",
                post(ROOT_URL + "/")
                        .body(OrderCreateRequest.class)
                        .handle(this::create),
                get( ROOT_URL + "/{cartId}")
                        .param(String.class)
                        .handle(this::queryOrder));
    }
}

