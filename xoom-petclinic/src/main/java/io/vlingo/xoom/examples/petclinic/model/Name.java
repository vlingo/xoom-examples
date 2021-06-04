package io.vlingo.xoom.examples.petclinic.model;

public final class Name {

  public final String value;

  public static Name from(final String value) {
    return new Name(value);
  }

  private Name (final String value) {
    this.value = value;
  }

}