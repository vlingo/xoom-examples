// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.flightplanning.infrastructure.persistence;

import com.vgoairlines.flightplanning.infrastructure.AircraftData;
import com.vgoairlines.flightplanning.infrastructure.FlightData;
import com.vgoairlines.flightplanning.model.aircraft.AircraftPooled;
import com.vgoairlines.flightplanning.model.aircraft.AircraftState;
import com.vgoairlines.flightplanning.model.flight.FlightCanceled;
import com.vgoairlines.flightplanning.model.flight.FlightRescheduled;
import com.vgoairlines.flightplanning.model.flight.FlightScheduled;
import com.vgoairlines.flightplanning.model.flight.FlightState;
import io.vlingo.xoom.turbo.annotation.persistence.*;
import io.vlingo.xoom.turbo.annotation.persistence.Persistence.StorageType;

@Persistence(basePackage = "com.vgoairlines.flightplanning", storageType = StorageType.STATE_STORE, cqrs = true)
@Projections({
  @Projection(actor = FlightProjectionActor.class, becauseOf = {FlightScheduled.class, FlightRescheduled.class, FlightCanceled.class}),
  @Projection(actor = AircraftProjectionActor.class, becauseOf = AircraftPooled.class)
})
@Adapters({
  FlightState.class, AircraftState.class
})
@EnableQueries({
  @QueriesEntry(protocol = FlightQueries.class, actor = FlightQueriesActor.class),
})
@DataObjects({FlightData.class, AircraftData.class})
public class PersistenceSetup {


}