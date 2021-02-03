// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.inventory.infrastructure;

import com.vgoairlines.inventory.model.aircraft.AircraftState;

import java.util.List;
import java.util.stream.Collectors;

public class AircraftData {
  public final String id;
  public final RegistrationData registration;
  public final ManufacturerSpecificationData manufacturerSpecification;
  public final CarrierData carrier;

  public static AircraftData from(final AircraftState state) {
    return new AircraftData(state);
  }

  public static List<AircraftData> from(final List<AircraftState> states) {
    return states.stream().map(AircraftData::from).collect(Collectors.toList());
  }

  public static AircraftData empty() {
    return new AircraftData(AircraftState.identifiedBy(null));
  }

  private AircraftData (final AircraftState state) {
    this.id = state.id;
    this.registration = new RegistrationData(state.registration.tailNumber);
    this.carrier = new CarrierData(state.carrier.name, state.carrier.type.name());
    this.manufacturerSpecification = new ManufacturerSpecificationData(state.manufacturerSpecification);
  }

}
