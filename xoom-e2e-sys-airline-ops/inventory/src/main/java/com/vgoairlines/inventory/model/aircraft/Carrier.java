// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.inventory.model.aircraft;

public final class Carrier {

  public final String name;
  public final CarrierType type;

  public static Carrier of(final String name, final CarrierType type) {
    return new Carrier(name, type);
  }

  private Carrier(final String name, final CarrierType type) {
    this.name = name;
    this.type = type;
  }

}
