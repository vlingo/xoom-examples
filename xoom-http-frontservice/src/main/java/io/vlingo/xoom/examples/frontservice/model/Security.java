// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.frontservice.model;

public final class Security {
  public final String publicToken;
  final String privateToken;

  public static Security from(String publicSecurityToken) {
    return new Security(publicSecurityToken);
  }

  public Security(final String publicToken) {
    this(publicToken, null);
  }

  public boolean isConfirmed() {
    return privateToken != null;
  }

  @Override
  public String toString() {
    final String privateTokenRepresentation = isConfirmed() ? "*******" : "???????";
    return "Security[publicToken=" + publicToken + ", privateToken=" + privateTokenRepresentation + "]";
  }

  Security withPrivateToken(final String privateToken) {
    return new Security(publicToken, privateToken);
  }

  Security(final String publicToken, final String privateToken) {
    assert(publicToken != null);

    this.publicToken = publicToken;
    this.privateToken = privateToken;
  }
}
