package com.vgoairlines.inventory.model.aircraft;

import io.vlingo.common.Completes;
import io.vlingo.lattice.model.stateful.StatefulEntity;

public final class AircraftEntity extends StatefulEntity<AircraftState> implements Aircraft {
  private AircraftState state;

  public AircraftEntity(final String id) {
    super(String.valueOf(id));
    this.state = AircraftState.identifiedBy(id);
  }

  public Completes<AircraftState> consign(final Registration registration,
                                          final ManufacturerSpecification manufacturerSpecification,
                                          final Carrier carrier) {
    final AircraftState stateArg = state.consign(registration, manufacturerSpecification, carrier);
    return apply(stateArg, new AircraftConsigned(stateArg), () -> state);
  }

  /*
   * Received when my current state has been applied and restored.
   *
   * @param state the AircraftState
   */
  @Override
  protected void state(final AircraftState state) {
    this.state = state;
  }

  /*
   * Received when I must provide my state type.
   *
   * @return {@code Class<AircraftState>}
   */
  @Override
  protected Class<AircraftState> stateType() {
    return AircraftState.class;
  }
}
