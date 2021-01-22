package com.vgoairlines.inventory.model.aircraft;

public final class Carrier {

  public final String name;
  public final CarrierType type;

  public static Carrier of(final String name, final CarrierType type) {
    return new Carrier(name, type);
  }

  private Carrier(final String name, final CarrierType type) {
    this.name = name;
    this.type = type;
  }

}
