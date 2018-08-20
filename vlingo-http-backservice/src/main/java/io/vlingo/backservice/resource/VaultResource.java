// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.backservice.resource;

import static io.vlingo.http.Response.Status.BadRequest;
import static io.vlingo.http.Response.Status.Ok;

import io.vlingo.auth.model.crypto.SCryptHasher;
import io.vlingo.http.Response;
import io.vlingo.http.resource.ResourceHandler;

public class VaultResource extends ResourceHandler {

  public VaultResource() { }

  public void generatePrivateToken(final String publicToken) {
    System.out.println("Request for private token of: " + publicToken);
    final SCryptHasher hasher = new SCryptHasher(16384, 8, 1);
    final String privateToken = hasher.hash(publicToken);
    if (hasher.verify(publicToken, privateToken)) {
      completes().with(Response.of(Ok, privateToken));
    } else {
      completes().with(Response.of(BadRequest, publicToken));
    }
  }
}
