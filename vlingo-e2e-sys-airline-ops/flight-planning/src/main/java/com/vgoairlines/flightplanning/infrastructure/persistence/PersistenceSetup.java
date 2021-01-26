package com.vgoairlines.flightplanning.infrastructure.persistence;

import com.vgoairlines.flightplanning.infrastructure.FlightData;
import com.vgoairlines.flightplanning.model.flight.*;
import io.vlingo.xoom.annotation.persistence.*;
import io.vlingo.xoom.annotation.persistence.Persistence.StorageType;

@Persistence(basePackage = "com.vgoairlines.flightplanning", storageType = StorageType.STATE_STORE, cqrs = true)
@Projections({
  @Projection(actor = FlightProjectionActor.class, becauseOf = {FlightScheduled.class, AircraftPooled.class, FlightRescheduled.class, FlightCanceled.class})
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