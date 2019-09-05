// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.backservice.resource;

import static io.vlingo.http.Response.Status.Ok;

import io.vlingo.auth.model.crypto.SCryptHasher;
import io.vlingo.backservice.infra.persistence.EventJournal;
import io.vlingo.backservice.resource.model.PrivateTokenGernerated;
import io.vlingo.http.RequestHeader;
import io.vlingo.http.Response;
import io.vlingo.http.ResponseHeader;
import io.vlingo.http.resource.ResourceHandler;

public class VaultResource extends ResourceHandler {
  public VaultResource() {
  }

  public void generatePrivateToken(final String publicToken) {
    final String id = context().request().headerValueOr(RequestHeader.XCorrelationID, "");
    logger().debug("GEN TOKEN FOR: " + publicToken + " WITH ID: " + id);
    completes().with(Response.of(Ok).include(ResponseHeader.of(ResponseHeader.XCorrelationID, id)));
    final SCryptHasher hasher = new SCryptHasher(16384, 8, 1);
    final String privateToken = hasher.hash(publicToken);
    EventJournal.provider().instance().append(new PrivateTokenGernerated(id, privateToken));
  }
}
