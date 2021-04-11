// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.airtrafficcontrol.model.controller;

import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.lattice.model.stateful.StatefulEntity;

public final class ControllerEntity extends StatefulEntity<ControllerState> implements Controller {
  private ControllerState state;

  public ControllerEntity(final String id) {
    super(String.valueOf(id));
    this.state = ControllerState.identifiedBy(id);
  }

  public Completes<ControllerState> authorize(final String name) {
    final ControllerState stateArg = state.authorize(name);
    return apply(stateArg, new ControllerAuthorized(stateArg), () -> state);
  }

  /*
   * Received when my current state has been applied and restored.
   *
   * @param state the ControllerState
   */
  @Override
  protected void state(final ControllerState state) {
    this.state = state;
  }

  /*
   * Received when I must provide my state type.
   *
   * @return {@code Class<ControllerState>}
   */
  @Override
  protected Class<ControllerState> stateType() {
    return ControllerState.class;
  }
}
