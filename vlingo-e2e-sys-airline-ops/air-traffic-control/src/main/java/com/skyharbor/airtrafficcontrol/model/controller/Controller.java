// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.airtrafficcontrol.model.controller;

import io.vlingo.actors.Definition;
import io.vlingo.actors.Address;
import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;

public interface Controller {

  Completes<ControllerState> authorize(final String name);

  static Completes<ControllerState> authorize(final Stage stage, final String name) {
    final Address address = stage.addressFactory().uniquePrefixedWith("g-");
    final Controller controller = stage.actorFor(Controller.class, Definition.has(ControllerEntity.class, Definition.parameters(address.idString())), address);
    return controller.authorize(name);
  }

}