package io.examples.calculation.endpoint.v1;

import io.examples.calculation.application.CalculationApplicationService;
import io.examples.calculation.application.ExecuteCalculation;
import io.examples.calculation.endpoint.CalculationEndpoint;
import io.vlingo.common.Completes;
import io.vlingo.http.Response;
import io.vlingo.http.resource.RequestHandler;
import io.vlingo.http.resource.ResourceBuilder;
import io.vlingo.xoom.resource.Endpoint;
import io.vlingo.xoom.resource.annotations.Resource;

import static io.vlingo.http.Response.Status.Ok;

/**
 * This {@code CalculationResource} exposes a REST API that maps resource HTTP request-response handlers to operations
 * contained in the {@link CalculationApplicationService}. This {@link Endpoint} implementation forms an anti-corruption layer between
 * consuming services and this microservice's {@link CalculationApplicationService} API.
 * <p>
 * This resource is a versioned API definition that implements the {@link CalculationEndpoint}. To fork versions, create a
 * separate implementation of the {@link CalculationEndpoint} in a separate package and change the getRequestHandlers
 * method by incrementing the versioned URI root.
 *
 * @author Danilo Ambrosio
 * @see CalculationEndpoint
 */
@Resource
public class CalculationResource implements CalculationEndpoint {

    private static final String ENDPOINT_VERSION = "1.1";
    private final CalculationApplicationService calculationApplicationService;

    public CalculationResource(final CalculationApplicationService calculationApplicationService) {
        this.calculationApplicationService = calculationApplicationService;
    }

    @Override
    public Completes<Response> calculate(final ExecuteCalculation executeCalculation) {
        return response(Ok, calculationApplicationService.calculate(executeCalculation));
    }

    @Override
    public Completes<Response> retrieveSupportedOperations() {
        return response(Ok, calculationApplicationService.retrieveSupportedOperations());
    }

    public RequestHandler[] getHandlers() {
        return new RequestHandler[]{
                ResourceBuilder.post("/v1/calculations")
                        .body(ExecuteCalculation.class)
                        .handle(this::calculate)
                        .onError(this::getErrorResponse),
                ResourceBuilder.get("/v1/calculations/operations")
                        .handle(this::retrieveSupportedOperations)
                        .onError(this::getErrorResponse)
        };
    }

    @Override
    public String getVersion() {
        return ENDPOINT_VERSION;
    }
}
