// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.


package io.examples.calculation.endpoint;

import io.examples.infrastructure.Bootstrap;
import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;
import io.vlingo.http.Request;
import io.vlingo.http.Response;
import io.vlingo.http.resource.Client;
import io.vlingo.http.resource.ResponseConsumer;
import io.vlingo.wire.node.Address;
import io.vlingo.wire.node.AddressType;
import io.vlingo.wire.node.Host;

import static io.vlingo.http.resource.Client.Configuration.defaultedKeepAliveExceptFor;

public class TestHttpClient {

    private static TestHttpClient instance;
    private Client httpClient;

    public static TestHttpClient instance(final String host, final int port) throws Exception {
        if(instance == null) {
            instance = new TestHttpClient(host, port);
        }
        return instance;
    }

    public TestHttpClient(final String host, final int port) throws Exception {
        final Stage stage =
                Bootstrap.instance().world().stage();

        final Address address =
                Address.from(Host.of(host), port, AddressType.NONE);

        final ResponseConsumer responseConsumer =
                (response) -> System.out.println("Unknown response received: " + response.status);

        this.httpClient = Client.using(defaultedKeepAliveExceptFor(stage, address, responseConsumer));
    }

    public Completes<Response> requestWith(final Request request) {
        return this.httpClient.requestWith(request);
    }

    public void close() {
        this.httpClient.close();
        instance = null;
    }

}
