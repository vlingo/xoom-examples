// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.backservice.resource.model;

public class PrivateTokenGenerated {
  public final String id;
  public final String hash;

  public PrivateTokenGenerated(final String id, final String hash) {
    this.id = id;
    this.hash = hash;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("PrivateTokenGenerated{");
    sb.append("id='").append(id).append('\'');
    sb.append(", hash='").append(hash).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
