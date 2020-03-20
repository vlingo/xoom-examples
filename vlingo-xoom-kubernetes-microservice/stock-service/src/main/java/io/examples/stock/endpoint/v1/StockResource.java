package io.examples.stock.endpoint.v1;

import io.examples.infrastructure.ApplicationRegistry;
import io.examples.infrastructure.StockQueryProvider;
import io.examples.stock.domain.ItemId;
import io.examples.stock.domain.Location;
import io.examples.stock.domain.Stock;
import io.examples.stock.endpoint.StockEndpoint;
import io.vlingo.actors.World;
import io.vlingo.common.Completes;
import io.vlingo.http.Response;
import io.vlingo.http.resource.RequestHandler;
import io.vlingo.xoom.resource.Endpoint;
import io.vlingo.xoom.resource.annotations.Resource;

import static io.vlingo.http.Response.Status.Ok;
import static io.vlingo.http.resource.ResourceBuilder.*;

/**
 * This {@code CalculationResource} exposes a REST API that maps resource HTTP request-response handlers to operations
 * contained in the {@link Stock}. This {@link Endpoint} implementation forms an anti-corruption layer between
 * consuming services and this microservice's {@link Stock} API.
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

    private final ApplicationRegistry applicationRegistry;

    public StockResource(final ApplicationRegistry applicationRegistry) {
        this.applicationRegistry = applicationRegistry;
    }

    @Override
    public Completes<Response> openStock(final OpenStock openStock) {
        final World world = applicationRegistry.retrieveWorld();
        final Location location = Location.valueOf(openStock.locationName());
        return response(Ok, Stock.openIn(world.stage(), location));
    }

    @Override
    public Completes<Response> loadStock(final AddItems addItems) {
        final World world = applicationRegistry.retrieveWorld();
        final ItemId itemId = ItemId.of(addItems.itemId());
        final Location location = Location.valueOf(addItems.locationName());
        return response(Ok, Stock.increaseAvailabilityFor(world.stage(), location, itemId, addItems.quantity()));
    }

    @Override
    public Completes<Response> findByLocation(final String locationName) {
        final Location location = Location.valueOf(locationName);
        return response(Ok, StockQueryProvider.instance().queries().queryByLocation(location));
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
