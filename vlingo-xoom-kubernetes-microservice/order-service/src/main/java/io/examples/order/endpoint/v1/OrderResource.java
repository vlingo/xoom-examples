package io.examples.order.endpoint.v1;

import io.examples.infrastructure.ApplicationRegistry;
import io.examples.infrastructure.OrderQueryProvider;
import io.examples.order.domain.Order;
import io.examples.order.domain.OrderState;
import io.examples.order.domain.ProductId;
import io.examples.order.domain.Site;
import io.examples.order.endpoint.OrderEndpoint;
import io.vlingo.common.Completes;
import io.vlingo.http.Response;
import io.vlingo.http.resource.RequestHandler;
import io.vlingo.xoom.resource.Endpoint;
import io.vlingo.xoom.resource.annotations.Resource;

import static io.vlingo.http.Response.Status.Ok;
import static io.vlingo.http.resource.ResourceBuilder.get;
import static io.vlingo.http.resource.ResourceBuilder.post;

/**
 * This {@code CalculationResource} exposes a REST API that maps resource HTTP request-response handlers to operations
 * contained in the {@link Order}. This {@link Endpoint} implementation forms an anti-corruption layer between
 * consuming services and this microservice's {@link Order} API.
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

    private final ApplicationRegistry applicationRegistry;

    public OrderResource(final ApplicationRegistry applicationRegistry) {
        this.applicationRegistry = applicationRegistry;
    }

    @Override
    public String getVersion() {
        return ENDPOINT_VERSION;
    }

    @Override
    public Completes<Response> orderProduct(final RegisterOrder registerOrder) {
        final Site site = Site.valueOf(registerOrder.siteName());
        final ProductId productId = ProductId.of(registerOrder.productId());
        final Completes<OrderState> order =
                Order.register(applicationRegistry, productId, registerOrder.quantity(), site);

        return response(Ok, order);
    }

    @Override
    public Completes<Response> listAll() {
        return response(Ok, OrderQueryProvider.instance().queries().allOrders());
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
