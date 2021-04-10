// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.backservice.resource;

import static io.vlingo.xoom.http.Response.Status.Ok;

import io.vlingo.xoom.examples.backservice.infra.persistence.EventJournal;
import io.vlingo.xoom.examples.backservice.resource.model.PrivateTokenGenerated;
import io.vlingo.xoom.common.crypto.SCryptHasher;
import io.vlingo.xoom.http.RequestHeader;
import io.vlingo.xoom.http.Response;
import io.vlingo.xoom.http.ResponseHeader;
import io.vlingo.xoom.http.resource.ResourceHandler;

public class VaultResource extends ResourceHandler {
  public VaultResource() {
  }

  /**
   * Name of this method must be provided in src/main/resources/xoom-http.properties as key=resource.name.vault
   * @param publicToken must be provided in curl --request GET /tokens/{publicToken}
   */
  public void generatePrivateToken(final String publicToken) {
    final String id = context().request().headerValueOr(RequestHeader.XCorrelationID, "");
    logger().debug("GEN TOKEN FOR: " + publicToken + " WITH ID: " + id);
    completes().with(Response.of(Ok).include(ResponseHeader.of(ResponseHeader.XCorrelationID, id)));
    final SCryptHasher hasher = new SCryptHasher(16384, 8, 1);
    final String privateToken = hasher.hash(publicToken);
    EventJournal.provider().instance().append(new PrivateTokenGenerated(id, privateToken));
  }
}
