package io.vlingo.examples.ecommerce.model;

import static io.vlingo.http.Response.Status.Created;
import static io.vlingo.http.Response.Status.NotFound;
import static io.vlingo.http.Response.Status.Ok;
import static io.vlingo.http.ResponseHeader.Location;
import static io.vlingo.http.ResponseHeader.headers;
import static io.vlingo.http.ResponseHeader.of;
import static io.vlingo.http.resource.ResourceBuilder.get;
import static io.vlingo.http.resource.ResourceBuilder.post;
import static io.vlingo.http.resource.ResourceBuilder.resource;

import java.util.HashMap;
import java.util.Map;

import io.vlingo.actors.Address;
import io.vlingo.actors.AddressFactory;
import io.vlingo.actors.Definition;
import io.vlingo.actors.Stage;
import io.vlingo.actors.World;
import io.vlingo.common.Completes;
import io.vlingo.http.resource.ObjectResponse;
import io.vlingo.http.resource.Resource;

public class OrderResource {
    private static final String ROOT_URL = "/order";
    private final AddressFactory addressFactory;
    private final Stage stage;

    public OrderResource(final World world) {
        this.addressFactory = world.addressFactory();
        this.stage = world.stage();
    }

    private Completes<ObjectResponse<String>> create(final OrderCreateRequest request) {
        final Address orderAddress = addressFactory.uniquePrefixedWith("order-");
        Map<ProductId, Integer> quantityByProductId = new HashMap<>();
        request.quantityByIdOfProduct.forEach((key, value) -> quantityByProductId.put(new ProductId(key), value));


        Order orderActor = stage.actorFor(
                Order.class,
                Definition.has(OrderActor.class,
                        Definition.parameters(orderAddress.idString())),
                orderAddress);

        orderActor.initOrderForUserProducts(request.userId, quantityByProductId);
        return Completes.withSuccess(
                ObjectResponse.of(Created,
                        headers(of(Location, urlLocation(orderAddress.idString()))),
                        ""));
    }

    private Completes<ObjectResponse<String>> postPayment(String orderId, PaymentId paymentId) {
        return stage.actorOf(Order.class, addressFactory.from(orderId))
                    .andThenConsume(actor -> {
                        actor.paymentComplete(paymentId);
                    })
                    .andThen(actor -> ObjectResponse.of(Ok, ""))
                    .otherwise(noOrder -> ObjectResponse.of(NotFound, ""));
    }

    private Completes<ObjectResponse<OrderInfo>> queryOrder(String orderId) {
        return stage.actorOf(Order.class, addressFactory.from(orderId))
                    .andThenTo(Order::query)
                    .andThenTo(orderInfo -> Completes.withSuccess(ObjectResponse.of(Ok, orderInfo)))
                    .otherwise(noOrder -> ObjectResponse.of(NotFound, OrderInfo.empty(orderId)));
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

