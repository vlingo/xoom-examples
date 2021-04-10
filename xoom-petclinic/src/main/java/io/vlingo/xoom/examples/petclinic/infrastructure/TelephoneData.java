package io.vlingo.xoom.examples.petclinic.infrastructure;

public class TelephoneData {

  public final String number;

  public static TelephoneData of(final String number) {
    return new TelephoneData(number);
  }

  private TelephoneData (final String number) {
    this.number = number;
  }

}