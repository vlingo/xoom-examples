package io.vlingo.xoom.examples.petclinic.infrastructure;

import io.vlingo.xoom.examples.petclinic.model.Telephone;

public class TelephoneData {

  public final String number;

  public static TelephoneData from(final Telephone telephone) {
    return from(telephone.number);
  }

  public static TelephoneData from(final String number) {
    return new TelephoneData(number);
  }

  private TelephoneData (final String number) {
    this.number = number;
  }

}