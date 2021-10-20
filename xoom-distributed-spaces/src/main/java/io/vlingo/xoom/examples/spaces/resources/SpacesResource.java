package io.vlingo.xoom.examples.spaces.resources;

import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.http.Response;
import io.vlingo.xoom.http.resource.DynamicResourceHandler;
import io.vlingo.xoom.http.resource.Resource;
import io.vlingo.xoom.http.resource.ResourceBuilder;
import io.vlingo.xoom.lattice.grid.Grid;
import io.vlingo.xoom.lattice.grid.spaces.*;
import io.vlingo.xoom.examples.spaces.model.Key1;

import java.time.Duration;
import java.util.Optional;

public class SpacesResource extends DynamicResourceHandler {

    private static final String accessorName = "distributed-accessor";
    private static final String spaceName = "distributed-space";

    public SpacesResource(final Stage stage) {
        super(stage);
    }

    private Completes<Response> get(String key) {
        Accessor accessor = Accessor.named((Grid) stage(), accessorName);
        if (accessor.isNotDefined()) {
            accessor = Accessor.using((Grid) stage(), accessorName);
        }

        Space space = accessor.distributedSpaceFor(spaceName);
        return space.get(new Key1(key), Period.None)
                .andThen(keyItem1 -> keyItem1
                        .map(keyItem2 -> Response.of(Response.Status.Ok, (String) keyItem2.object))
                        .orElse(Response.of(Response.Status.NotFound)));
    }

    private Completes<Response> put(SpaceData data) {
        Accessor accessor = Accessor.named((Grid) stage(), accessorName);
        if (accessor.isNotDefined()) {
            accessor = Accessor.using((Grid) stage(), accessorName);
        }

        Space space = accessor.distributedSpaceFor(spaceName);
        final Key1 key1 = new Key1(data.key);

        return space.put(key1, Item.of(data.value, Lease.Forever))
                .andThen(item -> Response.of(Response.Status.Ok));
    }

    private Completes<Response> delete(String key) {
        Accessor accessor = Accessor.named((Grid) stage(), accessorName);
        if (accessor.isNotDefined()) {
            accessor = Accessor.using((Grid) stage(), accessorName);
        }

        Space space = accessor.distributedSpaceFor(spaceName, 1, Duration.ofMillis(1_000));
        Completes<Optional<KeyItem<Object>>> takeCompletes = space.take(new Key1(key), Period.None);

        return takeCompletes
                .andThen(keyItem1 -> keyItem1
                        .map(keyItem2 -> Response.of(Response.Status.Ok, (String) keyItem2.object))
                        .orElse(Response.of(Response.Status.NotFound)));
    }

    @Override
    public Resource<?> routes() {
        return ResourceBuilder.resource("Distributed Spaces",
                ResourceBuilder.get("/spaces/{key}")
                        .param(String.class)
                        .handle(this::get),
                ResourceBuilder.post("/spaces")
                        .body(SpaceData.class)
                        .handle(this::put),
                ResourceBuilder.delete("/spaces/{key}")
                        .param(String.class)
                        .handle(this::delete));
    }
}
