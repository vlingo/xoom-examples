package com.vgoairlines.inventory.model.aircraft;

import io.vlingo.symbio.store.object.StateObject;

public final class Registration extends StateObject {

  public final String tailNumber;

  public static Registration of(final String tailNumber) {
    return new Registration(tailNumber);
  }

  private Registration(final String tailNumber) {
    this.tailNumber = tailNumber;
  }

}
