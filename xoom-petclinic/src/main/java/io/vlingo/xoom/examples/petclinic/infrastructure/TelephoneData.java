package io.vlingo.xoom.examples.petclinic.infrastructure;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vlingo.xoom.examples.petclinic.model.Telephone;

public class TelephoneData {

  public final String number;

  public static TelephoneData from(final Telephone telephone) {
    return from(telephone.number);
  }

  @JsonCreator
  public static TelephoneData from(@JsonProperty("number") final String number) {
    return new TelephoneData(number);
  }

  private TelephoneData (final String number) {
    this.number = number;
  }

}