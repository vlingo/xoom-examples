// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.inventory.model.aircraft;

import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.lattice.model.stateful.StatefulEntity;

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
