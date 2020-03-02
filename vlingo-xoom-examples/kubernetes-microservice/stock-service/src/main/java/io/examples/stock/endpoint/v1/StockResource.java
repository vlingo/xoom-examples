package io.examples.stock.endpoint.v1;

import io.examples.stock.application.AddItems;
import io.examples.stock.application.OpenStock;
import io.examples.stock.application.StockApplicationServices;
import io.examples.stock.endpoint.StockEndpoint;
import io.vlingo.common.Completes;
import io.vlingo.http.Response;
import io.vlingo.http.resource.RequestHandler;
import io.vlingo.xoom.resource.Endpoint;
import io.vlingo.xoom.resource.annotations.Resource;

import static io.vlingo.http.Response.Status.Ok;
import static io.vlingo.http.resource.ResourceBuilder.*;

/**
 * This {@code CalculationResource} exposes a REST API that maps resource HTTP request-response handlers to operations
 * contained in the {@link StockApplicationServices}. This {@link Endpoint} implementation forms an anti-corruption layer between
 * consuming services and this microservice's {@link StockApplicationServices} API.
 * <p>
 * This resource is a versioned API definition that implements the {@link StockEndpoint}. To fork versions, create a
 * separate implementation of the {@link StockEndpoint} in a separate package and change the getRequestHandlers
 * method by incrementing the versioned URI root.
 *
 * @author Danilo Ambrosio
 * @see StockEndpoint
 */
@Resource
public class StockResource implements StockEndpoint {

    private static String ENDPOINT_VERSION = "1.1";

    private final StockApplicationServices stockApplicationServices;

    public StockResource(final StockApplicationServices stockApplicationServices) {
        this.stockApplicationServices = stockApplicationServices;
    }

    @Override
    public Completes<Response> openStock(final OpenStock openStock) {
        return response(Ok, stockApplicationServices.openStock(openStock));
    }

    @Override
    public Completes<Response> loadStock(final AddItems addItems) {
        return response(Ok, stockApplicationServices.loadStock(addItems));
    }

    @Override
    public Completes<Response> findByLocation(final String locationName) {
        return response(Ok, stockApplicationServices.findByLocation(locationName));
    }

    @Override
    public String getVersion() {
        return ENDPOINT_VERSION;
    }

    @Override
    public RequestHandler[] getHandlers() {
        return new RequestHandler[]{
                post("/v1/stocks")
                        .body(OpenStock.class)
                        .handle(this::openStock)
                        .onError(this::getErrorResponse),
                put("/v1/stocks")
                        .body(AddItems.class)
                        .handle(this::loadStock)
                        .onError(this::getErrorResponse),
                get("/v1/stocks")
                        .query("locationName")
                        .handle(this::findByLocation)
                        .onError(this::getErrorResponse)
        };
    }
}
