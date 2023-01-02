// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.inventory.model.aircraft;

import io.vlingo.xoom.symbio.store.object.StateObject;

public final class Registration extends StateObject {

  public final String tailNumber;

  public static Registration of(final String tailNumber) {
    return new Registration(tailNumber);
  }

  private Registration(final String tailNumber) {
    this.tailNumber = tailNumber;
  }

}
