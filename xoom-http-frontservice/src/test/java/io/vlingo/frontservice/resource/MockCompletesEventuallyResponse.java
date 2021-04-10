// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.frontservice.resource;

import io.vlingo.xoom.actors.Address;
import io.vlingo.xoom.actors.CompletesEventually;
import io.vlingo.xoom.actors.testkit.TestUntil;
import io.vlingo.xoom.http.Response;

public class MockCompletesEventuallyResponse implements CompletesEventually {
  public static TestUntil untilWith;

  public Response response;
  
  @Override
  public Address address() {
    return null;
  }

  @Override
  public void with(final Object outcome) {
    this.response = (Response) outcome;
    if (untilWith != null) untilWith.happened();
  }
}
