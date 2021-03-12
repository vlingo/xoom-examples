package io.vlingo.developers.petclinic.infrastructure;

public class NameData {

  public final String value;

  public static NameData of(final String value) {
    return new NameData(value);
  }

  private NameData (final String value) {
    this.value = value;
  }

}