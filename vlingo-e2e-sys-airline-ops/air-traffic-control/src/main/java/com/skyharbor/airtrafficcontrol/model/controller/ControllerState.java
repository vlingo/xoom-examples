package com.skyharbor.airtrafficcontrol.model.controller;

import io.vlingo.symbio.store.object.StateObject;

public final class ControllerState extends StateObject {

  public final String id;
  public final String name;

  public static ControllerState identifiedBy(final String id) {
    return new ControllerState(id, null);
  }

  public ControllerState (final String id, final String name) {
    this.id = id;
    this.name = name;
  }

  public ControllerState authorize(final String name) {
    return new ControllerState(this.id, name);
  }

}
