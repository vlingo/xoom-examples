package io.vlingo.xoom.examples.petclinic.infrastructure;

public class FullnameData {

  public final String first;
  public final String last;

  public static FullnameData of(final String first, final String last) {
    return new FullnameData(first, last);
  }

  private FullnameData (final String first, final String last) {
    this.first = first;
    this.last = last;
  }

}