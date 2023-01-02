// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.inventory.infrastructure;

import com.vgoairlines.inventory.model.aircraft.ManufacturerSpecification;

public class ManufacturerSpecificationData {
  public final String manufacturer;
  public final String model;
  public final String serialNumber;

  public ManufacturerSpecificationData(final String manufacturer,
                                       final String model,
                                       final String serialNumber) {
    this.manufacturer = manufacturer;
    this.model = model;
    this.serialNumber = serialNumber;
  }

  public ManufacturerSpecificationData(final ManufacturerSpecification manufacturerSpecification) {
    this(manufacturerSpecification.manufacturer, manufacturerSpecification.model, manufacturerSpecification.serialNumber);
  }
}
