// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.inventory.model.aircraft;

public final class ManufacturerSpecification {

  public final String manufacturer;
  public final String model;
  public final String serialNumber;

  public static ManufacturerSpecification of(final String manufacturer, final String model, final String serialNumber) {
    return new ManufacturerSpecification(manufacturer, model, serialNumber);
  }

  private ManufacturerSpecification(String manufacturer, String model, String serialNumber) {
    this.manufacturer = manufacturer;
    this.model = model;
    this.serialNumber = serialNumber;
  }

}
