// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.saasovation.agilepm.model.product;

import java.util.UUID;

public class ProductId {
  public final String id;
  
  public static ProductId fromExisting(final String referencedId) {
    return new ProductId(referencedId);
  }

  public static ProductId unique() {
    return new ProductId();
  }

  private ProductId() {
    this(UUID.randomUUID().toString());
  }

  private ProductId(final String id) {
    this.id = id;
  }
}
