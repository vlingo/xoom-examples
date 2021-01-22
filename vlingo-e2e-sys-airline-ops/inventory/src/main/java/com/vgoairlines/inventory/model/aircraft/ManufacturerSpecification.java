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
