package io.examples.order.infra.web;

import io.examples.order.domain.Order;
import io.examples.order.application.OrderService;
import io.vlingo.common.Completes;
import io.vlingo.http.Response;
import io.vlingo.http.resource.RequestHandler;
import io.vlingo.xoom.resource.annotations.Resource;
import io.vlingo.xoom.resource.Endpoint;

import javax.inject.Provider;

import static io.vlingo.http.Response.Status.Created;
import static io.vlingo.http.Response.Status.Ok;
import static io.vlingo.http.resource.ResourceBuilder.get;
import static io.vlingo.http.resource.ResourceBuilder.post;

@Resource
public class OrderApiResource implements Endpoint {

    private static final String ENDPOINT_VERSION = "1.0.0";
    private static final String ENDPOINT_NAME = "Order";
    private final Provider<OrderService> orderService;

    public OrderApiResource(Provider<OrderService> orderService) {
        this.orderService = orderService;
    }

    @Override
    public RequestHandler[] getHandlers() {
        return new RequestHandler[]{
                post("/v1/orders")
                        .body(Order.class)
                        .handle(this::defineOrder)
                        .onError(this::getErrorResponse),
                get("/v1/orders/{id}")
                        .param(Long.class)
                        .handle(this::queryOrder)
                        .onError(this::getErrorResponse)
        };
    }

    private Completes<Response> defineOrder(Order order) {
        return response(Created, orderService.get().defineOrder(order));
    }

    private Completes<Response> queryOrder(Long id) {
        return response(Ok, orderService.get().queryOrder(id));
    }

    @Override
    public String getName() {
        return ENDPOINT_NAME;
    }

    @Override
    public String getVersion() {
        return OrderApiResource.ENDPOINT_VERSION;
    }
}
