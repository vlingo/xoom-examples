// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package com.vgoairlines.flightplanning.model.aircraft;

import io.vlingo.actors.Address;
import io.vlingo.actors.Definition;
import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;

public interface Aircraft {

  static Completes<AircraftState> pool(final Stage stage,
                                       final String aircraftId,
                                       final Denomination denomination) {
    final Address _address = stage.addressFactory().uniquePrefixedWith("g-");
    final Aircraft aircraft = stage.actorFor(Aircraft.class, Definition.has(AircraftEntity.class, Definition.parameters(_address.idString())), _address);
    return aircraft.pool(aircraftId, denomination);
  }

  Completes<AircraftState> pool(final String aircraftId, final Denomination denomination);

}
