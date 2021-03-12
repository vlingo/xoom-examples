package io.vlingo.developers.petclinic.model.client;

public class Name {

  public final String value;

  public static Name of(final String value) {
    return new Name(value);
  }

  private Name (final String value) {
    this.value = value;
  }

}