// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.rest;

import static io.vlingo.http.ResponseHeader.Location;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.vlingo.actors.Definition;
import io.vlingo.actors.World;
import io.vlingo.actors.testkit.AccessSafely;
import io.vlingo.http.Filters;
import io.vlingo.http.Response;
import io.vlingo.http.ResponseHeader;
import io.vlingo.http.resource.Configuration.Sizing;
import io.vlingo.http.resource.Configuration.Timing;
import io.vlingo.http.resource.Resources;
import io.vlingo.http.resource.Server;
import io.vlingo.rest.TestResponseChannelConsumer.Progress;
import io.vlingo.wire.channel.ResponseChannelConsumer;
import io.vlingo.wire.fdx.bidirectional.BasicClientRequestResponseChannel;
import io.vlingo.wire.fdx.bidirectional.ClientRequestResponseChannel;
import io.vlingo.wire.message.ByteBufferAllocator;
import io.vlingo.wire.message.Converters;
import io.vlingo.wire.node.Address;
import io.vlingo.wire.node.AddressType;
import io.vlingo.wire.node.Host;

public class OrganizationResourceTest {
  private static final AtomicInteger baseServerPort = new AtomicInteger(18080);

  private final ByteBuffer buffer = ByteBufferAllocator.allocate(1024);
  private ClientRequestResponseChannel client;
  private ResponseChannelConsumer consumer;
  private Progress progress;
  private OrganizationResource resource;
  private Server server;
  private int serverPort;
  private World world;
  
  @Test
  public void testThatResouceAnswers() {
    final Response startResponse = resource.defineOrganization().await();
    Assert.assertNotNull(startResponse);
    final String id = resourceId(startResponse);
    Assert.assertNotNull(id);
    Assert.assertFalse(startResponse.entity.content().isEmpty());
    
    final Response queryResponse = resource.queryOrganization(id).await();
    Assert.assertNotNull(queryResponse);
    Assert.assertEquals(id, queryResponse.entity.content());
  }
  
  @Test
  public void testThatRoutesMap() {
    client.requestWith(toByteBuffer(postRequest()));
    
    final Response createdResponse = pollClient();

    Assert.assertEquals(1, progress.consumeCount.get());
    Assert.assertNotNull(createdResponse.headers.headerOf(ResponseHeader.Location));

    final String id = resourceId(createdResponse);
    client.requestWith(toByteBuffer(getRequest(id)));
    
    final Response queryResponse = pollClient();

    Assert.assertEquals(2, progress.consumeCount.get());
    Assert.assertEquals(id, queryResponse.entity.content());
    
    client.requestWith(toByteBuffer(patchRequest(id, "NEWNAME")));
    
    final Response patchResponse = pollClient();

    Assert.assertEquals(3, progress.consumeCount.get());
    Assert.assertEquals("NEWNAME", patchResponse.entity.content());
  }

  @Before
  public void setUp() throws Exception {
    world = World.startWithDefaults("OrganizationsResourceTest");
    
    resource = new OrganizationResource(world);
    
    serverPort = baseServerPort.getAndIncrement();
    
    server =
            Server.startWith(
                    world.stage(),
                    Resources.are(resource.routes()),
                    Filters.none(),
                    serverPort,
                    Sizing.defineWith(4, 10, 10, 1024),
                    Timing.defineWith(3, 1, 100));

    progress = new Progress();

    consumer = world.actorFor(ResponseChannelConsumer.class, Definition.has(TestResponseChannelConsumer.class, Definition.parameters(progress)));

    client = new BasicClientRequestResponseChannel(Address.from(Host.of("localhost"), serverPort, AddressType.NONE), consumer, 100, 10240, world.defaultLogger());
  }
  
  @After
  public void tearDown() {
    server.stop();
    
    world.terminate();
  }
  
  private String getRequest(final String id) {
    return "GET /organizations/" + id + " HTTP/1.1\nHost: vlingo.io\n\n";
  }
  
  private String patchRequest(final String id, final String name) {
    return "PATCH /organizations/" + id + "/name HTTP/1.1\nHost: vlingo.io\nContent-Length: " + name.length() + "\n\n" + name;
  }

  private Response pollClient() {
    final AccessSafely consumeCalls = progress.expectConsumeTimes(1);
    while (consumeCalls.totalWrites() < 1) {
      client.probeChannel();
    }
    consumeCalls.readFrom("completed");

    final Response response = progress.responses.poll();

    return response;
  }
  
  private String postRequest() {
    return "POST /organizations HTTP/1.1\nHost: vlingo.io\n\n";
  }

  private String resourceId(final Response response) {
    final String location = response.headerValueOr(Location, null);
    final String id = location.substring(location.lastIndexOf('/') + 1);
    return id;
  }
  
  private ByteBuffer toByteBuffer(final String requestContent) {
    buffer.clear();
    buffer.put(Converters.textToBytes(requestContent));
    buffer.flip();
    return buffer;
  }
}
