// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.backservice.resource;

import static io.vlingo.http.Response.Status.Ok;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.vlingo.actors.World;
import io.vlingo.actors.testkit.TestUntil;
import io.vlingo.http.Context;
import io.vlingo.http.Request;
import io.vlingo.http.resource.Action;
import io.vlingo.http.resource.ConfigurationResource;
import io.vlingo.http.resource.Dispatcher;
import io.vlingo.http.resource.Resource;
import io.vlingo.http.resource.ResourceHandler;
import io.vlingo.http.resource.Resources;
import io.vlingo.wire.message.ByteBufferAllocator;
import io.vlingo.wire.message.Converters;

public class VaultResourceTest {
  private static final String publicToken = "jfhf90r8re978er88e,ndf!--88dh*";
  private static final String getPrivateTokenMessage =
          "GET /tokens/" + publicToken + " HTTP/1.1\nHost: vlingo.io\n\n";

  private Action actionGetPrivateToken;
  private final ByteBuffer buffer = ByteBufferAllocator.allocate(65535);
  private Dispatcher dispatcher;
  private World world;

  @Test
  public void testThatVaultProcessesPrivateTokenWithOk() {
    final Request request = Request.from(toByteBuffer(getPrivateTokenMessage));
    final MockCompletesEventuallyResponse completes = new MockCompletesEventuallyResponse();

    MockCompletesEventuallyResponse.untilWith = TestUntil.happenings(1);
    dispatcher.dispatchFor(new Context(request, completes));
    MockCompletesEventuallyResponse.untilWith.completes();

    assertNotNull(completes.response);
    assertEquals(Ok, completes.response.status);
  }

  @Before
  @SuppressWarnings("unchecked")
  public void setUp() throws Exception {
    world = World.startWithDefaults("test-vault");

    actionGetPrivateToken = new Action(0, "GET", "/tokens/{publicToken}", "generatePrivateToken(String publicToken)", null);
    final List<Action> actions = Arrays.asList(actionGetPrivateToken);

    final Class<? extends ResourceHandler> resourceHandlerClass =
            (Class<? extends ResourceHandler>) Class.forName("io.vlingo.backservice.resource.VaultResource");

    final Resource<?> resource = ConfigurationResource.defining("vault", resourceHandlerClass, 5, actions);

    dispatcher = Dispatcher.startWith(world.stage(), Resources.are(resource));
  }

  @After
  public void tearDown() {
    world.terminate();
  }

  private ByteBuffer toByteBuffer(final String requestContent) {
    buffer.clear();
    buffer.put(Converters.textToBytes(requestContent));
    buffer.flip();
    return buffer;
  }
}
