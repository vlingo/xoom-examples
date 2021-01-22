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
