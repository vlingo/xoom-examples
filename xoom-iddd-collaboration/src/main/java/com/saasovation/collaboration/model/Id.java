// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.saasovation.collaboration.model;

import java.util.UUID;

public abstract class Id {
  public final String value;

  @Override
  public int hashCode() {
    return value.hashCode();
  }

  @Override
  public boolean equals(final Object other) {
    if (other == null || other.getClass() != getClass()) {
      return false;
    }
    
    final Id otherId = (Id) other;
    
    return this.value.equals(otherId.value);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "[value=" + value + "]";
  }

  protected Id() {
    this(UUID.randomUUID().toString());
  }

  protected Id(final String referencedId) {
    this.value = referencedId;
  }
}
