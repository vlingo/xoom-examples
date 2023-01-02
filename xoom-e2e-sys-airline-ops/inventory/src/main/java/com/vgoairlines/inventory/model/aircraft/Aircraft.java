// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.inventory.model.aircraft;

import io.vlingo.xoom.actors.Address;
import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.common.Completes;

public interface Aircraft {

  Completes<AircraftState> consign(final Registration registration, final ManufacturerSpecification manufacturerSpecification, final Carrier carrier);

  static Completes<AircraftState> consign(final Stage stage, final Registration registration, final ManufacturerSpecification manufacturerSpecification, final Carrier carrier) {
    final Address _address = stage.addressFactory().uniquePrefixedWith("g-");
    final Aircraft _aircraft = stage.actorFor(Aircraft.class, Definition.has(AircraftEntity.class, Definition.parameters(_address.idString())), _address);
    return _aircraft.consign(registration, manufacturerSpecification, carrier);
  }

}