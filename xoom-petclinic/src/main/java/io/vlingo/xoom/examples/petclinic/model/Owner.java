package io.vlingo.xoom.examples.petclinic.model;

public final class Owner {

  public final String clientId;

  public static Owner from(final String clientId) {
    return new Owner(clientId);
  }

  private Owner (final String clientId) {
    this.clientId = clientId;
  }

}