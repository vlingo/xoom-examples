// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.inventory.infrastructure;

import io.vlingo.symbio.store.object.StateObject;

public final class RegistrationData extends StateObject {

  public final String tailNumber;

  public RegistrationData(final String tailNumber) {
    this.tailNumber = tailNumber;
  }

}
