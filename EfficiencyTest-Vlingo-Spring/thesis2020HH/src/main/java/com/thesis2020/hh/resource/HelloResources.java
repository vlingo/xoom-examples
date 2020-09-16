package com.thesis2020.hh.resource;

import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;
import io.vlingo.http.Response;
import io.vlingo.http.resource.Resource;
import io.vlingo.http.resource.ResourceBuilder;
import io.vlingo.http.resource.ResourceHandler;

import static io.vlingo.http.Response.Status.Ok;
import static io.vlingo.http.resource.ResourceBuilder.get;

public class HelloResources extends ResourceHandler {


    private final Stage stage;
    private static final String Hello = "Hello, #!";
    private static final String World = "World";


    public HelloResources(final Stage stage){
        this.stage = stage;
    }

    @Override
    public Resource<?> routes() {
        return ResourceBuilder.resource("helloWorld Resources",
                get("/hello")
                        .handle(this::hello),
                get("/hello/{whom}")
                        .param(String.class)
                        .handle(this::helloWhom));

    }

    public Completes<Response> hello() {
        return helloWhom(World);
    }

    public Completes<Response> helloWhom(String whom) {
        return Completes.withSuccess(Response.of(Ok, Hello.replace("#", whom)));
    }
}
