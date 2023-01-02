// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.airtrafficcontrol.infrastructure;

import com.skyharbor.airtrafficcontrol.model.controller.ControllerState;

import java.util.List;
import java.util.stream.Collectors;

public class ControllerData {
  public final String id;
  public final String name;

  public static ControllerData from(final ControllerState state) {
    return new ControllerData(state);
  }

  public static List<ControllerData> from(final List<ControllerState> states) {
    return states.stream().map(ControllerData::from).collect(Collectors.toList());
  }

  public static ControllerData empty() {
    return new ControllerData(ControllerState.identifiedBy(null));
  }

  private ControllerData (final ControllerState state) {
    this.id = state.id;
    this.name = state.name;
  }

}
