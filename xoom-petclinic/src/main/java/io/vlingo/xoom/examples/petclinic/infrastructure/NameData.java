package io.vlingo.xoom.examples.petclinic.infrastructure;

import io.vlingo.xoom.examples.petclinic.model.Name;

public class NameData {

  public final String value;

  public static NameData from(final Name name) {
    return from(name.value);
  }

  public static NameData from(final String value) {
    return new NameData(value);
  }

  private NameData (final String value) {
    this.value = value;
  }

}