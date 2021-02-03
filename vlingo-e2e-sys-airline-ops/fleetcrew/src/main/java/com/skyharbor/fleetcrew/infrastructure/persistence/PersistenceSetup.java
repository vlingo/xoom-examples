// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.fleetcrew.infrastructure.persistence;

import com.skyharbor.fleetcrew.model.aircraft.*;
import io.vlingo.xoom.annotation.persistence.Persistence;
import io.vlingo.xoom.annotation.persistence.Persistence.StorageType;
import io.vlingo.xoom.annotation.persistence.Projections;
import io.vlingo.xoom.annotation.persistence.Projection;
import io.vlingo.xoom.annotation.persistence.Adapters;
import io.vlingo.xoom.annotation.persistence.EnableQueries;
import io.vlingo.xoom.annotation.persistence.QueriesEntry;
import io.vlingo.xoom.annotation.persistence.DataObjects;
import com.skyharbor.fleetcrew.infrastructure.AircraftData;

@Persistence(basePackage = "com.skyharbor.fleetcrew", storageType = StorageType.STATE_STORE, cqrs = true)
@Projections({
  @Projection(actor = AircraftProjectionActor.class, becauseOf = {GateReassigned.class, CargoLoaded.class, CargoUnloaded.class,
          DepartureRecorded.class, ArrivalRecorded.class, ArrivalPlanned.class})
})
@Adapters({
  AircraftState.class
})
@EnableQueries({
  @QueriesEntry(protocol = AircraftQueries.class, actor = AircraftQueriesActor.class),
})
@DataObjects({AircraftData.class})
public class PersistenceSetup {


}