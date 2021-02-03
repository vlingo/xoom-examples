// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.aircraftmonitoring.infrastructure.persistence;

import io.vlingo.xoom.annotation.persistence.Persistence;
import io.vlingo.xoom.annotation.persistence.Persistence.StorageType;
import io.vlingo.xoom.annotation.persistence.Projections;
import io.vlingo.xoom.annotation.persistence.Projection;
import io.vlingo.xoom.annotation.persistence.Adapters;
import io.vlingo.xoom.annotation.persistence.EnableQueries;
import io.vlingo.xoom.annotation.persistence.QueriesEntry;
import io.vlingo.xoom.annotation.persistence.DataObjects;
import com.skyharbor.aircraftmonitoring.model.flight.Landed;
import com.skyharbor.aircraftmonitoring.model.flight.FlightState;
import com.skyharbor.aircraftmonitoring.model.flight.InFlight;
import com.skyharbor.aircraftmonitoring.model.flight.LocationReported;
import com.skyharbor.aircraftmonitoring.model.flight.DepartedGate;
import com.skyharbor.aircraftmonitoring.infrastructure.FlightData;
import com.skyharbor.aircraftmonitoring.model.flight.ArrivedAtGate;

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