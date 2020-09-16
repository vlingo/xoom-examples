package com.thesis2020.hh.resource;


import io.vlingo.actors.AddressFactory;
import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;
import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.http.Response;
import io.vlingo.http.resource.RequestHandler;
import io.vlingo.http.resource.Resource;
import io.vlingo.http.resource.ResourceBuilder;
import io.vlingo.http.resource.ResourceHandler;

import static io.vlingo.http.resource.ResourceBuilder.resource;

import com.thesis2020.hh.infrastructure.data.GreetingData;
import com.thesis2020.hh.infrastructure.data.UpdateGreetingData;
import com.thesis2020.hh.infrastructure.persistence.Queries;
import com.thesis2020.hh.infrastructure.persistence.QueryModelStateStoreProvider;
import com.thesis2020.hh.model.greeting.Greeting;
import com.thesis2020.hh.model.greeting.GreetingEntity;

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

    private RequestHandler postGreeting(){
        return ResourceBuilder.post("/greetings")
                .body(GreetingData.class)
                .handle(this::defineGreeting);
    }

    private RequestHandler getGreetingWithID(){
        return ResourceBuilder.get("/greetings/{greetingId}")
                .param(String.class)
                .handle(this::getGreeting);
    }

    private RequestHandler changeMessage(){
        return ResourceBuilder.patch("/greetings/{greetingId}/message")
                .param(String.class)
                .body(UpdateGreetingData.class)
                .handle(this::changeGreetingMessage);
    }

    private RequestHandler changeDescription(){
        return ResourceBuilder.patch("/greetings/{greetingId}/description")
                .param(String.class)
                .body(UpdateGreetingData.class)
                .handle(this::changeGreetingDescription);
    }




    private Completes<Response> defineGreeting(GreetingData data){
        return  Greeting.defineGreeting(stage,data)
                .andThenTo(state -> Completes.withSuccess(Response.of(Response.Status.Created, JsonSerialization.serialized(state))));
    }

    private Completes<Response> getGreeting(String greetingId){
        return queries.greetingWithId(greetingId)
                .andThenTo(state -> Completes.withSuccess(Response.of(Response.Status.Ok, JsonSerialization.serialized(state))))
                .otherwise(nodata -> Response.of(Response.Status.NotFound,notFoundMessage(greetingId)));
    }

    private Completes<Response> changeGreetingMessage(String greetingId,UpdateGreetingData data){
        return resolve(greetingId)
                .andThenTo(greeting -> greeting.updateMessage(data))
                .andThenTo(state -> Completes.withSuccess(Response.of(Response.Status.Ok,JsonSerialization.serialized(state))))
                .otherwise(nodata -> Response.of(Response.Status.NotFound,notFoundMessage(greetingId)));
    }

    private Completes<Response> changeGreetingDescription(String greetingId, UpdateGreetingData data){
        return resolve(greetingId)
                .andThenTo(greeting -> greeting.updateDescription(data))
                .andThenTo(state -> Completes.withSuccess(Response.of(Response.Status.Ok,JsonSerialization.serialized(state))))
                .otherwise(nodata -> Response.of(Response.Status.NotFound,notFoundMessage(greetingId)));
    }

    private Completes<Greeting> resolve(final String greetingId) {
        return stage.actorOf(Greeting.class, addressFactory.from(greetingId), GreetingEntity.class);
    }


    private String notFoundMessage(String id){
        return "Greeting for ID: " + id + " not found";
    }



}