package com.vgoairlines.inventory.infrastructure.persistence;

import com.vgoairlines.inventory.infrastructure.AircraftData;
import com.vgoairlines.inventory.model.aircraft.AircraftConsigned;
import com.vgoairlines.inventory.model.aircraft.AircraftState;
import io.vlingo.xoom.annotation.persistence.*;
import io.vlingo.xoom.annotation.persistence.Persistence.StorageType;

@Persistence(basePackage = "com.vgoairlines.inventory", storageType = StorageType.STATE_STORE, cqrs = true)
@Projections({
  @Projection(actor = AircraftProjectionActor.class, becauseOf = {AircraftConsigned.class})
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