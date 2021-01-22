package com.vgoairlines.inventory.model.aircraft;

import io.vlingo.symbio.store.object.StateObject;

public final class AircraftState extends StateObject {

  public final String id;
  public final Registration registration;
  public final ManufacturerSpecification manufacturerSpecification;
  public final Carrier carrier;

  public static AircraftState identifiedBy(final String id) {
    return new AircraftState(id, null, null, null);
  }

  public AircraftState (final String id,
                        final Registration registration,
                        final ManufacturerSpecification manufacturerSpecification,
                        final Carrier carrier) {
    this.id = id;
    this.registration = registration;
    this.manufacturerSpecification = manufacturerSpecification;
    this.carrier = carrier;
  }

  public AircraftState consign(final Registration registration,
                               final ManufacturerSpecification manufacturerSpecification,
                               final Carrier carrier) {
    return new AircraftState(this.id, registration, manufacturerSpecification, carrier);
  }

}
