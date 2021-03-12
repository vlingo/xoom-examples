package io.vlingo.developers.petclinic.model.client;

public class Owner {

  public final String clientId;

  public static Owner of(final String clientId) {
    return new Owner(clientId);
  }

  private Owner (final String clientId) {
    this.clientId = clientId;
  }

}