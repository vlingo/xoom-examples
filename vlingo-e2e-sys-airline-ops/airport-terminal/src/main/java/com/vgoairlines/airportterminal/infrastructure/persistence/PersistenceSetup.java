package com.vgoairlines.airportterminal.infrastructure.persistence;

import io.vlingo.xoom.annotation.persistence.Persistence;
import io.vlingo.xoom.annotation.persistence.Persistence.StorageType;
import io.vlingo.xoom.annotation.persistence.Projections;
import io.vlingo.xoom.annotation.persistence.Projection;
import io.vlingo.xoom.annotation.persistence.Adapters;
import io.vlingo.xoom.annotation.persistence.EnableQueries;
import io.vlingo.xoom.annotation.persistence.QueriesEntry;
import io.vlingo.xoom.annotation.persistence.DataObjects;
import com.vgoairlines.airportterminal.model.flight.FlightState;
import com.vgoairlines.airportterminal.model.flight.GateOpened;
import com.vgoairlines.airportterminal.model.flight.BoardingCompleted;
import com.vgoairlines.airportterminal.model.gateagent.GateAgentRegistered;
import com.vgoairlines.airportterminal.model.flight.BoardingStarted;
import com.vgoairlines.airportterminal.infrastructure.GateAgentData;
import com.vgoairlines.airportterminal.model.flight.FlightDeparted;
import com.vgoairlines.airportterminal.infrastructure.FlightData;
import com.vgoairlines.airportterminal.model.gateagent.GateAgentState;
import com.vgoairlines.airportterminal.model.flight.GateClosed;

@Persistence(basePackage = "com.vgoairlines.airportterminal", storageType = StorageType.STATE_STORE, cqrs = true)
@Projections({
  @Projection(actor = FlightProjectionActor.class, becauseOf = {BoardingStarted.class, GateClosed.class, GateOpened.class, FlightDeparted.class, BoardingCompleted.class}),
  @Projection(actor = GateAgentProjectionActor.class, becauseOf = {GateAgentRegistered.class})
})
@Adapters({
  FlightState.class,
  GateAgentState.class
})
@EnableQueries({
  @QueriesEntry(protocol = FlightQueries.class, actor = FlightQueriesActor.class),
  @QueriesEntry(protocol = GateAgentQueries.class, actor = GateAgentQueriesActor.class),
})
@DataObjects({GateAgentData.class, FlightData.class})
public class PersistenceSetup {


}