// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.inventory.model.aircraft;

import io.vlingo.xoom.symbio.store.object.StateObject;

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
