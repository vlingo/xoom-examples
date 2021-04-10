package io.vlingo.xoom.examples.petclinic.model;

public class Fullname {

  public final String first;
  public final String last;

  public static Fullname of(final String first, final String last) {
    return new Fullname(first, last);
  }

  private Fullname (final String first, final String last) {
    this.first = first;
    this.last = last;
  }

}