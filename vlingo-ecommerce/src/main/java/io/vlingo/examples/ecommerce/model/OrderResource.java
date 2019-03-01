package io.vlingo.examples.ecommerce.model;

import io.vlingo.actors.*;
import io.vlingo.common.Completes;
import io.vlingo.http.Response;
import io.vlingo.http.resource.Resource;

import java.util.HashMap;
import java.util.Map;

import static io.vlingo.common.serialization.JsonSerialization.serialized;
import static io.vlingo.http.Response.Status.*;
import static io.vlingo.http.ResponseHeader.*;
import static io.vlingo.http.resource.ResourceBuilder.*;

public class OrderResource {
    private static final String ROOT_URL = "/order";
    private final AddressFactory addressFactory;
    private final Stage stage;

    public OrderResource(final World world) {
        this.addressFactory = world.addressFactory();
        this.stage = world.stage();
    }

    private Completes<Response> create(final OrderCreateRequest request) {
        final Address orderAddress = addressFactory.uniquePrefixedWith("order-");
        Map<ProductId, Integer> quantityByProductId = new HashMap<>();
        request.quantityByIdOfProduct.forEach((key, value) -> quantityByProductId.put(new ProductId(key), value));


        Order orderActor = stage.actorFor(
                Order.class,
                Definition.has(OrderEntity.class,
                        Definition.parameters(orderAddress.idString())),
                orderAddress);

        orderActor.initOrderForUserProducts(request.userId, quantityByProductId);
        return Completes.withSuccess(
                Response.of(Created,
                        headers(of(Location, urlLocation(orderAddress.idString()))),
                        ""));
    }

    private Completes<Response> postPayment(String orderId, PaymentId paymentId) {
        Completes<Response> x = stage.actorOf(Order.class, addressFactory.from(orderId))
                    .andThenConsume(actor -> {
                        actor.paymentComplete(paymentId);
                    })
                    .andThen(actor -> Response.of(Ok, ""))
                    .otherwise(noOrder -> Response.of(NotFound, urlLocation(orderId)));

        x.await();
        return x;
    }

    private Completes<Response> queryOrder(String orderId) {
        return stage.actorOf(Order.class, addressFactory.from(orderId))
                    .andThenTo(Order::query)
                    .andThenTo(orderInfo -> Completes.withSuccess(Response.of(Ok, serialized(orderInfo))))
                    .otherwise(noOrder -> Response.of(NotFound, urlLocation(orderId)));
    }

    private String urlLocation(final String orderId) {
        return ROOT_URL + "/" + orderId;
    }

    public Resource<?> routes() {
        return resource("Order resource fluent api",
                post(ROOT_URL)
                        .body(OrderCreateRequest.class)
                        .handle(this::create),
                get(ROOT_URL + "/{orderId}")
                        .param(String.class)
                        .handle(this::queryOrder),
                post(ROOT_URL + "/{orderId}/payment")
                        .param(String.class)
                        .body(PaymentId.class)
                        .handle(this::postPayment));
    }

    public static class OrderCreateRequest {
        final Map<String, Integer> quantityByIdOfProduct;
        final UserId userId;

        OrderCreateRequest(Map<String, Integer> quantityByIdOfProduct, UserId userId) {
            this.quantityByIdOfProduct = quantityByIdOfProduct;
            this.userId = userId;
        }

        public static class Builder {
            private Map<String, Integer> quantityByIdOfProduct;
            private UserId userId;

            private Builder() {
                quantityByIdOfProduct = new HashMap<>();
            }

            public static Builder create() {
                return new Builder();
            }

            public Builder withProduct(ProductId productId, Integer quantity) {
                quantityByIdOfProduct.put(productId.id, quantity);
                return this;
            }

            public Builder withUser(UserId userId) {
                this.userId = userId;
                return this;
            }

            public OrderCreateRequest build() {
                return new OrderCreateRequest(quantityByIdOfProduct, userId);
            }
        }
    }

}

