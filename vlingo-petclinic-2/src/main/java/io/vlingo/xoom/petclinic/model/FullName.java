package io.vlingo.xoom.petclinic.model;

public final class FullName {

  public final String first;
  public final String last;

  public static FullName from(final String first, final String last) {
    return new FullName(first, last);
  }

  private FullName (final String first, final String last) {
    this.first = first;
    this.last = last;
  }

}