package com.thesis2020.hh.resource;


import io.vlingo.actors.AddressFactory;
import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;
import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.http.Response;
import io.vlingo.http.resource.RequestHandler;
import io.vlingo.http.resource.Resource;
import io.vlingo.http.resource.ResourceHandler;

import static io.vlingo.http.resource.ResourceBuilder.resource;
import static io.vlingo.http.Response.Status.Ok;
import static io.vlingo.http.Response.Status.Created;
import static io.vlingo.http.Response.Status.NotFound;
import static io.vlingo.http.resource.ResourceBuilder.get;
import static io.vlingo.http.resource.ResourceBuilder.post;
import static io.vlingo.http.resource.ResourceBuilder.patch;

import com.thesis2020.hh.infrastructure.data.GreetingRequestData;
import com.thesis2020.hh.infrastructure.data.GreetingChangeRequestData;
import com.thesis2020.hh.infrastructure.data.GreetingData;
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
        this.queries = QueryModelStateStoreProvider.INSTANCE.queris;
    }

    @Override
    public Resource<?> routes() {

        return resource("GreetingResource", 20,
                postGreeting(),
                getGreetingWithID(),
                changeGreetingMessage(),
                changeGreetingDescription());
    }

    private RequestHandler postGreeting(){
        return post("/greetings")
                .body(GreetingRequestData.class)
                .handle(this::defineGreeting);
    }

    private RequestHandler getGreetingWithID(){
        return get("/greetings/{greetingId}")
                .param(String.class)
                .handle(this::getGreeting);
    }

    private RequestHandler changeGreetingMessage(){
        return patch("/greetings/{greetingId}/message")
                .param(String.class)
                .body(GreetingChangeRequestData.class)
                .handle(this::changeMessage);
        
    }

    private RequestHandler changeGreetingDescription(){
        return patch("/greetings/{greetingId}/description")
                .param(String.class)
                .body(GreetingChangeRequestData.class)
                .handle(this::changeDescription);
    }

    private Completes<Response> defineGreeting(GreetingRequestData data){
        return  Greeting.defineGreeting(stage,data)
                .andThenTo(state -> Completes.withSuccess(Response.of(Created, JsonSerialization.serialized(GreetingData.from(state)))));
    }

    private Completes<Response> getGreeting(String greetingId){
        return queries.greetingWithId(greetingId)
                .andThenTo(data -> Completes.withSuccess(Response.of(Ok, JsonSerialization.serialized(data))))
                .otherwise(nodata -> Response.of(NotFound,notFoundMessage(greetingId)));
    }

    private Completes<Response> changeMessage(String greetingId,GreetingChangeRequestData data){
        return resolve(greetingId)
                .andThenTo(greeting -> greeting.changeMessage(data))
                .andThenTo(state -> Completes.withSuccess(Response.of(Ok,JsonSerialization.serialized(GreetingData.from(state)))))
                .otherwise(nodata -> Response.of(NotFound,notFoundMessage(greetingId)));
    }

    private Completes<Response> changeDescription(String greetingId, GreetingChangeRequestData data){
        return resolve(greetingId)
                .andThenTo(greeting -> greeting.changeDescription(data))
                .andThenTo(state -> Completes.withSuccess(Response.of(Ok,JsonSerialization.serialized(GreetingData.from(state)))))
                .otherwise(nodata -> Response.of(NotFound,notFoundMessage(greetingId)));
    }

    private Completes<Greeting> resolve(final String greetingId) {
        return stage.actorOf(Greeting.class, addressFactory.from(greetingId), GreetingEntity.class);
    }


    private String notFoundMessage(String id){
        return "Greeting for ID: " + id + " not found";
    }



}