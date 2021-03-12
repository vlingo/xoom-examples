package io.vlingo.developers.petclinic.model;

public class Telephone {

  public final String number;

  public static Telephone of(final String number) {
    return new Telephone(number);
  }

  private Telephone (final String number) {
    this.number = number;
  }

}