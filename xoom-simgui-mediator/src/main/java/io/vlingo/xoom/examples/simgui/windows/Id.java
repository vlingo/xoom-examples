// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.simgui.windows;

import java.util.concurrent.atomic.AtomicInteger;

public class Id {
  private static AtomicInteger nextId = new AtomicInteger(0);

  public final int value;

  public static Id fromExisting(final int value) {
    return new Id(value);
  }

  public static Id unique() {
    return new Id();
  }

  public Id() {
    this.value = nextId.incrementAndGet();
  }

  public Id(final int value) {
    this.value = value;
  }

  @Override
  public int hashCode() {
    return 31 * value;
  }

  @Override
  public boolean equals(final Object other) {
    if (other == null || other.getClass() != getClass()) {
      return false;
    }
    final Id otherId = (Id) other;
    return value == otherId.value;
  }

  @Override
  public String toString() {
    return "Id[value=" + value + "]";
  }
}
