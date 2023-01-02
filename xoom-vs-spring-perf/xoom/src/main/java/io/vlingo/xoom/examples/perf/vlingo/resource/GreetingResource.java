// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.perf.vlingo.resource;

import io.vlingo.xoom.actors.AddressFactory;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.common.serialization.JsonSerialization;
import io.vlingo.xoom.examples.perf.vlingo.infrastructure.data.GreetingData;
import io.vlingo.xoom.examples.perf.vlingo.infrastructure.data.UpdateGreetingData;
import io.vlingo.xoom.examples.perf.vlingo.infrastructure.persistence.Queries;
import io.vlingo.xoom.examples.perf.vlingo.infrastructure.persistence.QueryModelStateStoreProvider;
import io.vlingo.xoom.examples.perf.vlingo.model.greeting.Greeting;
import io.vlingo.xoom.examples.perf.vlingo.model.greeting.GreetingEntity;
import io.vlingo.xoom.http.Response;
import io.vlingo.xoom.http.resource.RequestHandler;
import io.vlingo.xoom.http.resource.Resource;
import io.vlingo.xoom.http.resource.ResourceBuilder;
import io.vlingo.xoom.http.resource.ResourceHandler;

import static io.vlingo.xoom.http.resource.ResourceBuilder.resource;

public class GreetingResource extends ResourceHandler {

    private final Stage stage;
    private final Queries queries;
    private final AddressFactory addressFactory;

    public GreetingResource(final Stage stage) {
        this.stage = stage;
        this.addressFactory = stage.addressFactory();
        this.queries = QueryModelStateStoreProvider.instance().queries;
    }

    @Override
    public Resource<?> routes() {
        return resource("GreetingResource",
                postGreeting(),
                getGreetingWithID(),
                changeMessage(),
                changeDescription());
    }

    private RequestHandler postGreeting() {
        return ResourceBuilder.post("/greetings")
                .body(GreetingData.class)
                .handle(this::defineGreeting);
    }

    private RequestHandler getGreetingWithID() {
        return ResourceBuilder.get("/greetings/{greetingId}")
                .param(String.class)
                .handle(this::getGreeting);
    }

    private RequestHandler changeMessage() {
        return ResourceBuilder.patch("/greetings/{greetingId}/message")
                .param(String.class)
                .body(UpdateGreetingData.class)
                .handle(this::changeGreetingMessage);
    }

    private RequestHandler changeDescription() {
        return ResourceBuilder.patch("/greetings/{greetingId}/description")
                .param(String.class)
                .body(UpdateGreetingData.class)
                .handle(this::changeGreetingDescription);
    }


    private Completes<Response> defineGreeting(GreetingData data) {
        return Greeting.defineGreeting(stage, data)
                .andThenTo(state -> Completes.withSuccess(Response.of(Response.Status.Created, JsonSerialization.serialized(state))));
    }

    private Completes<Response> getGreeting(String greetingId) {
        return queries.greetingWithId(greetingId)
                .andThenTo(state -> Completes.withSuccess(Response.of(Response.Status.Ok, JsonSerialization.serialized(state))))
                .otherwise(nodata -> Response.of(Response.Status.NotFound, notFoundMessage(greetingId)));
    }

    private Completes<Response> changeGreetingMessage(String greetingId, UpdateGreetingData data) {
        return resolve(greetingId)
                .andThenTo(greeting -> greeting.updateMessage(data))
                .andThenTo(state -> Completes.withSuccess(Response.of(Response.Status.Ok, JsonSerialization.serialized(state))))
                .otherwise(nodata -> Response.of(Response.Status.NotFound, notFoundMessage(greetingId)));
    }

    private Completes<Response> changeGreetingDescription(String greetingId, UpdateGreetingData data) {
        return resolve(greetingId)
                .andThenTo(greeting -> greeting.updateDescription(data))
                .andThenTo(state -> Completes.withSuccess(Response.of(Response.Status.Ok, JsonSerialization.serialized(state))))
                .otherwise(nodata -> Response.of(Response.Status.NotFound, notFoundMessage(greetingId)));
    }

    private Completes<Greeting> resolve(final String greetingId) {
        return stage.actorOf(Greeting.class, addressFactory.from(greetingId), GreetingEntity.class);
    }


    private String notFoundMessage(String id) {
        return "Greeting for ID: " + id + " not found";
    }

}