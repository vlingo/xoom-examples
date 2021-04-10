package io.vlingo.xoom.examples.petclinic.infrastructure;

import io.vlingo.xoom.examples.petclinic.model.FullName;

public class FullNameData {

  public final String first;
  public final String last;

  public static FullNameData from(final FullName fullName) {
    return from(fullName.first, fullName.last);
  }

  public static FullNameData from(final String first, final String last) {
    return new FullNameData(first, last);
  }

  private FullNameData (final String first, final String last) {
    this.first = first;
    this.last = last;
  }

}