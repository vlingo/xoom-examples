// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.perf.vlingo.resource;

import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.http.Response;
import io.vlingo.xoom.http.resource.Resource;
import io.vlingo.xoom.http.resource.ResourceBuilder;
import io.vlingo.xoom.http.resource.ResourceHandler;

import static io.vlingo.xoom.http.Response.Status.Ok;
import static io.vlingo.xoom.http.resource.ResourceBuilder.get;

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
