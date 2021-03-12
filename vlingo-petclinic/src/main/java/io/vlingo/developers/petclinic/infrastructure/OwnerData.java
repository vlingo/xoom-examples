package io.vlingo.developers.petclinic.infrastructure;

public class OwnerData {

  public final String clientId;

  public static OwnerData of(final String clientId) {
    return new OwnerData(clientId);
  }

  private OwnerData (final String clientId) {
    this.clientId = clientId;
  }

}