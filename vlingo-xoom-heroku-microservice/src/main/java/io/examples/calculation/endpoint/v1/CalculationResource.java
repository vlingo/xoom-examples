package io.examples.calculation.endpoint.v1;

import io.examples.calculation.domain.Calculation;
import io.examples.calculation.domain.CalculationState;
import io.examples.calculation.domain.Operation;
import io.examples.calculation.endpoint.CalculationEndpoint;
import io.examples.calculation.endpoint.ExecuteCalculation;
import io.examples.infrastructure.ApplicationRegistry;
import io.vlingo.actors.World;
import io.vlingo.common.Completes;
import io.vlingo.http.Response;
import io.vlingo.http.resource.RequestHandler;
import io.vlingo.http.resource.ResourceBuilder;
import io.vlingo.xoom.resource.Endpoint;
import io.vlingo.xoom.resource.annotations.Resource;

import static io.vlingo.http.Response.Status.Ok;

/**
 * This {@code CalculationResource} exposes a REST API that maps resource HTTP request-response handlers to operations
 * contained in the {@link Calculation}. This {@link Endpoint} implementation forms an anti-corruption layer between
 * consuming services and this microservice.
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

    private final ApplicationRegistry applicationRegistry;

    public CalculationResource(final ApplicationRegistry applicationRegistry) {
        this.applicationRegistry = applicationRegistry;
    }

    @Override
    public Completes<Response> calculate(final ExecuteCalculation executeCalculation) {
        final World world = applicationRegistry.retrieveWorld();
        final Integer firstOperand = executeCalculation.firstOperand();
        final Integer secondOperand = executeCalculation.secondOperand();
        final Operation operation = Operation.withName(executeCalculation.operationName());
        return response(Ok, Calculation.calculate(world.stage(), operation, firstOperand, secondOperand));
    }

    @Override
    public Completes<Response> retrieveSupportedOperations() {
        return response(Ok, Completes.withSuccess(Operation.values()));
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
