package io.vlingo.xoom.examples.petclinic.infrastructure;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vlingo.xoom.examples.petclinic.model.Owner;

public class OwnerData {

  public final String clientId;

  public static OwnerData from(final Owner owner) {
    return from(owner.clientId);
  }

  @JsonCreator
  public static OwnerData from(@JsonProperty("clientId") final String clientId) {
    return new OwnerData(clientId);
  }

  private OwnerData (final String clientId) {
    this.clientId = clientId;
  }

}