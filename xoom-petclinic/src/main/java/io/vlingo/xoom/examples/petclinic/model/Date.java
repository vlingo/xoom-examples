package io.vlingo.xoom.examples.petclinic.model;

public final class Date {

  public final long value;

  public static Date from(final long value) {
    return new Date(value);
  }

  private Date (final long value) {
    this.value = value;
  }

}