package io.examples.order.endpoint;

import io.examples.order.endpoint.v1.RegisterOrder;
import io.vlingo.common.Completes;
import io.vlingo.http.Response;
import io.vlingo.http.resource.RequestHandler;
import io.vlingo.xoom.resource.Endpoint;
import io.vlingo.xoom.resource.annotations.Resource;

/**
 * The {@link OrderEndpoint} describes a base REST API contract that is used to evolve
 * versions of your API without breaking consumers.
 * <p>
 * By implementing this interface and marking it with the {@link Resource} annotation,
 * you can override this base endpoint definition with your versioned changes. By overriding
 * the {@link RequestHandler[]} in Endpoint.getRequestHandlers(), you can serve different versions
 * of your REST API.
 *
 * @author Danilo Ambrosio
 * @see OrderEndpoint
 */
public interface OrderEndpoint extends Endpoint {

    String ENDPOINT_NAME = "Order";

    @Override
    default String getName() {
        return ENDPOINT_NAME;
    }

    Completes<Response> orderProduct(final RegisterOrder registerOrder);

    Completes<Response> listAll();

}
