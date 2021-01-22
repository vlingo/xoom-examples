package com.vgoairlines.inventory.model.aircraft;

import io.vlingo.actors.Definition;
import io.vlingo.actors.Address;
import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;

public interface Aircraft {

  Completes<AircraftState> consign(final Registration registration, final ManufacturerSpecification manufacturerSpecification, final Carrier carrier);

  static Completes<AircraftState> consign(final Stage stage, final Registration registration, final ManufacturerSpecification manufacturerSpecification, final Carrier carrier) {
    final Address _address = stage.addressFactory().uniquePrefixedWith("g-");
    final Aircraft _aircraft = stage.actorFor(Aircraft.class, Definition.has(AircraftEntity.class, Definition.parameters(_address.idString())), _address);
    return _aircraft.consign(registration, manufacturerSpecification, carrier);
  }

}