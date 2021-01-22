package com.vgoairlines.inventory.infrastructure;

import io.vlingo.symbio.store.object.StateObject;

public final class RegistrationData extends StateObject {

  public final String tailNumber;

  public RegistrationData(final String tailNumber) {
    this.tailNumber = tailNumber;
  }

}
