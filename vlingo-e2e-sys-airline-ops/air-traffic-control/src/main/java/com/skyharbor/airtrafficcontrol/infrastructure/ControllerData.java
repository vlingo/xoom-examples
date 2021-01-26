package com.skyharbor.airtrafficcontrol.infrastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.skyharbor.airtrafficcontrol.model.controller.ControllerState;

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
