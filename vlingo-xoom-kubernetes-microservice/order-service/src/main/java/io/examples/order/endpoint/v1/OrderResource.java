package io.examples.order.endpoint.v1;

import io.examples.order.application.OrderApplicationServices;
import io.examples.order.application.RegisterOrder;
import io.examples.order.endpoint.OrderEndpoint;
import io.vlingo.common.Completes;
import io.vlingo.http.Response;
import io.vlingo.http.resource.RequestHandler;
import io.vlingo.xoom.resource.Endpoint;
import io.vlingo.xoom.resource.annotations.Resource;

import static io.vlingo.common.Completes.withSuccess;
import static io.vlingo.http.Response.Status.Ok;
import static io.vlingo.http.resource.ResourceBuilder.get;
import static io.vlingo.http.resource.ResourceBuilder.post;

/**
 * This {@code CalculationResource} exposes a REST API that maps resource HTTP request-response handlers to operations
 * contained in the {@link OrderApplicationServices}. This {@link Endpoint} implementation forms an anti-corruption layer between
 * consuming services and this microservice's {@link OrderApplicationServices} API.
 * <p>
 * This resource is a versioned API definition that implements the {@link OrderEndpoint}. To fork versions, create a
 * separate implementation of the {@link OrderEndpoint} in a separate package and change the getRequestHandlers
 * method by incrementing the versioned URI root.
 *
 * @author Danilo Ambrosio
 * @see OrderEndpoint
 */
@Resource
public class OrderResource implements OrderEndpoint {

    private static String ENDPOINT_VERSION = "1.1";

    private final OrderApplicationServices orderApplicationServices;

    public OrderResource(final OrderApplicationServices orderApplicationServices) {
        this.orderApplicationServices = orderApplicationServices;
    }

    @Override
    public String getVersion() {
        return ENDPOINT_VERSION;
    }

    @Override
    public Completes<Response> orderProduct(final RegisterOrder registerOrder) {
        return response(Ok, orderApplicationServices.orderProduct(registerOrder));
    }

    @Override
    public Completes<Response> listAll() {
        return response(Ok, orderApplicationServices.allOrders());
    }

    @Override
    public RequestHandler[] getHandlers() {
        return new RequestHandler[]{
                post("/v1/orders")
                    .body(RegisterOrder.class)
                    .handle(this::orderProduct)
                    .onError(this::getErrorResponse),
                get("/v1/orders")
                    .handle(this::listAll)
                    .onError(this::getErrorResponse)
        };
    }

}
