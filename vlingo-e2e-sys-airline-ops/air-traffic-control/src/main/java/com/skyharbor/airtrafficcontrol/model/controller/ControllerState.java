// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.airtrafficcontrol.model.controller;

import io.vlingo.symbio.store.object.StateObject;

public final class ControllerState extends StateObject {

  public final String id;
  public final String name;

  public static ControllerState identifiedBy(final String id) {
    return new ControllerState(id, null);
  }

  public ControllerState (final String id, final String name) {
    this.id = id;
    this.name = name;
  }

  public ControllerState authorize(final String name) {
    return new ControllerState(this.id, name);
  }

}
