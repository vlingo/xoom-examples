package io.vlingo.xoom.petclinic.model;

public final class Telephone {

  public final String number;

  public static Telephone from(final String number) {
    return new Telephone(number);
  }

  private Telephone (final String number) {
    this.number = number;
  }

}