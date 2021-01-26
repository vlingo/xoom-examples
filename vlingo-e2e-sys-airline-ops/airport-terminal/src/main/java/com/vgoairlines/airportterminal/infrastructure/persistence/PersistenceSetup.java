package com.vgoairlines.airportterminal.infrastructure.persistence;

import com.vgoairlines.airportterminal.infrastructure.FlightData;
import com.vgoairlines.airportterminal.infrastructure.GateAgentData;
import com.vgoairlines.airportterminal.model.flight.*;
import com.vgoairlines.airportterminal.model.gateagent.GateAgentRegistered;
import com.vgoairlines.airportterminal.model.gateagent.GateAgentState;
import io.vlingo.xoom.annotation.persistence.*;
import io.vlingo.xoom.annotation.persistence.Persistence.StorageType;

@Persistence(basePackage = "com.vgoairlines.airportterminal", storageType = StorageType.STATE_STORE, cqrs = true)
@Projections({
  @Projection(actor = FlightProjectionActor.class, becauseOf = {BoardingStarted.class, GateClosed.class, GateOpened.class, FlightArrived.class, FlightDeparted.class, BoardingEnded.class}),
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