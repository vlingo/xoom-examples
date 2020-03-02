package io.examples.account.endpoint;

import io.vlingo.xoom.resource.annotations.Resource;
import io.vlingo.common.Completes;
import io.vlingo.xoom.config.ServerConfiguration;
import io.vlingo.http.Response;
import io.vlingo.http.resource.RequestHandler;
import io.vlingo.xoom.resource.Endpoint;

import static io.vlingo.http.Response.Status.Ok;
import static io.vlingo.http.resource.ResourceBuilder.get;

@Resource
public class PortEndpoint implements Endpoint {

    private static String ENDPOINT_VERSION = "1.1";
    private static String ENDPOINT_NAME = "Port Endpoint";
    private final ServerConfiguration serverConfiguration;

    public PortEndpoint(ServerConfiguration serverConfiguration) {
        this.serverConfiguration = serverConfiguration;
    }

    /**
     * Get the semantic version of this {@link PortEndpoint}.
     *
     * @return a {@link String} representing the semantic version of this HTTP {@link Endpoint} definition.
     */
    @Override
    public String getVersion() {
        return PortEndpoint.ENDPOINT_VERSION;
    }

    @Override
    public String getName() {
        return PortEndpoint.ENDPOINT_NAME;
    }

    @Override
    public RequestHandler[] getHandlers() {
        return new RequestHandler[]{
                get("/port")
                        .handle(this::getPort)
                        .onError(this::getErrorResponse)
        };
    }

    private Completes<Response> getPort() {
        return response(Ok, Completes.withSuccess(serverConfiguration.getPort()));
    }
}
