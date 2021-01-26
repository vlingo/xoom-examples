package com.skyharbor.airtrafficcontrol.model.controller;

import io.vlingo.actors.Definition;
import io.vlingo.actors.Address;
import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;

public interface Controller {

  Completes<ControllerState> authorize(final String name);

  static Completes<ControllerState> authorize(final Stage stage, final String name) {
    final Address _address = stage.addressFactory().uniquePrefixedWith("g-");
    final Controller _controller = stage.actorFor(Controller.class, Definition.has(ControllerEntity.class, Definition.parameters(_address.idString())), _address);
    return _controller.authorize(name);
  }

}