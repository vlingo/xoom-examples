// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.eventjournal.counter.events;

import io.vlingo.symbio.Source;

public abstract class Event extends Source<String> {
  public final int currentCounter;

  protected Event(final int currentCounter) {
    this.currentCounter = currentCounter;
  }

  public boolean isIncreased() { return false; } 
  public boolean isDecreased() { return false; }
}
