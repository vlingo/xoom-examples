// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.frontservice.resource;

import static io.vlingo.common.serialization.JsonSerialization.deserialized;
import static io.vlingo.common.serialization.JsonSerialization.serialized;
import static io.vlingo.http.Response.Status.Created;
import static io.vlingo.http.ResponseHeader.Location;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.nio.ByteBuffer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.vlingo.actors.World;
import io.vlingo.actors.testkit.TestUntil;
import io.vlingo.frontservice.data.ContactData;
import io.vlingo.frontservice.data.NameData;
import io.vlingo.frontservice.data.UserData;
import io.vlingo.frontservice.infra.Bootstrap;
import io.vlingo.http.Context;
import io.vlingo.http.Request;
import io.vlingo.http.resource.Dispatcher;
import io.vlingo.http.resource.Resources;
import io.vlingo.wire.message.ByteBufferAllocator;
import io.vlingo.wire.message.Converters;

public class UserTest {
  private static final UserData janeDoeUserData =
          UserData.from(
                  NameData.from("Jane", "Doe"),
                  ContactData.from("jane.doe@vlingo.io", "+1 212-555-1212"),
                  "jfhf90r8re978er88e,ndf!--88dh*");
  private static final String janeDoeUserSerialized = serialized(janeDoeUserData);
  private static final String postJaneDoeUserMessage =
          "POST /users HTTP/1.1\nHost: vlingo.io\nContent-Length: " + janeDoeUserSerialized.length() + "\n\n" + janeDoeUserSerialized;

  private final ByteBuffer buffer = ByteBufferAllocator.allocate(65535);
  private Dispatcher dispatcher;
  private World world;

  @Test
  public void testThatUserBasicRegisters() {
    final Request request = Request.from(toByteBuffer(postJaneDoeUserMessage));
    final MockCompletesEventuallyResponse completes = new MockCompletesEventuallyResponse();

    MockCompletesEventuallyResponse.untilWith = TestUntil.happenings(1);
    dispatcher.dispatchFor(new Context(request, completes));
    MockCompletesEventuallyResponse.untilWith.completes();

    assertNotNull(completes.response);

    assertEquals(Created, completes.response.status);
    assertEquals(2, completes.response.headers.size());
    assertEquals(Location, completes.response.headers.get(0).name);
    assertTrue(Location, completes.response.headerOf(Location).value.startsWith("/users/"));
    assertNotNull(completes.response.entity);

    final UserData createdUserData = deserialized(completes.response.entity.content, UserData.class);
    assertNotNull(createdUserData);
    assertEquals(janeDoeUserData.nameData.given, createdUserData.nameData.given);
    assertEquals(janeDoeUserData.nameData.family, createdUserData.nameData.family);
    assertEquals(janeDoeUserData.contactData.emailAddress, createdUserData.contactData.emailAddress);
    assertEquals(janeDoeUserData.contactData.telephoneNumber, createdUserData.contactData.telephoneNumber);
  }

  @Test
  public void testThatUserLongRunningRegisters() {
    
  }

  @Before
  public void setUp() throws Exception {
    world = Bootstrap.instance().world;
    final UserResource userResource = new UserResource(world);
    dispatcher = Dispatcher.startWith(world.stage(), Resources.are(userResource.routes()));
  }

  @After
  public void tearDown() {
    Bootstrap.terminateTestInstance();
    world.terminate();
  }

  private ByteBuffer toByteBuffer(final String requestContent) {
    buffer.clear();
    buffer.put(Converters.textToBytes(requestContent));
    buffer.flip();
    return buffer;
  }
}
