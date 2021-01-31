package com.thesis2020.hh.resource;

import io.vlingo.common.Completes;
import io.vlingo.http.Response;
import io.vlingo.http.resource.Resource;
import io.vlingo.http.resource.ResourceBuilder;
import io.vlingo.http.resource.ResourceHandler;

import static io.vlingo.http.Response.Status.Ok;
import static io.vlingo.http.resource.ResourceBuilder.get;

public class HelloResources extends ResourceHandler {


    private static final String Hello = "Hello, #!";
    private static final String World = "World";



    @Override
    public Resource<?> routes() {
        return ResourceBuilder.resource("helloWorld Resources", 20,
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
