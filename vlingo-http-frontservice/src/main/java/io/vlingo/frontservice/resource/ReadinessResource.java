package io.vlingo.frontservice.resource;

import io.vlingo.actors.Stage;
import io.vlingo.actors.World;
import io.vlingo.http.Response;
import io.vlingo.http.resource.ResourceHandler;

import static io.vlingo.http.Response.Status.Ok;

public class ReadinessResource extends ResourceHandler {
    private final Stage stage;

    public ReadinessResource(final World world) {
        this.stage = world.stage();
    }

    void readiness() {
        completes().with(Response.of(Ok));
    }
}
