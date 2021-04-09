// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.entity;

import java.util.UUID;

public final class Id {
  public final String value;

  public static Id from(final String value) {
    return new Id(value);
  }

  public static Id unique() {
    return new Id();
  }

  public Id() {
    this(UUID.randomUUID().toString());
  }

  public Id(final String value) {
    this.value = value;
  }

  public final boolean isDefined() {
    return value.length() > 0;
  }

  public final boolean isUndefined() {
    return value.isEmpty();
  }
}
