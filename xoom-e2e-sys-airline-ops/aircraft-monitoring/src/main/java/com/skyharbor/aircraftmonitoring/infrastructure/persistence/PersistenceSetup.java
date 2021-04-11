// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.aircraftmonitoring.infrastructure.persistence;

import com.skyharbor.aircraftmonitoring.infrastructure.FlightData;
import com.skyharbor.aircraftmonitoring.model.flight.*;
import io.vlingo.xoom.turbo.annotation.persistence.*;
import io.vlingo.xoom.turbo.annotation.persistence.Persistence.StorageType;

@Persistence(basePackage = "com.skyharbor.aircraftmonitoring", storageType = StorageType.STATE_STORE, cqrs = true)
@Projections({
  @Projection(actor = FlightProjectionActor.class, becauseOf = {Landed.class, DepartedGate.class, LocationReported.class, InFlight.class, ArrivedAtGate.class})
})
@Adapters({
  FlightState.class
})
@EnableQueries({
  @QueriesEntry(protocol = FlightQueries.class, actor = FlightQueriesActor.class),
})
@DataObjects({FlightData.class})
public class PersistenceSetup {


}