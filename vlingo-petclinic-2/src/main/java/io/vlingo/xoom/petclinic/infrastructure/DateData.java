package io.vlingo.xoom.petclinic.infrastructure;

import io.vlingo.xoom.petclinic.model.Date;

public class DateData {

  public final long value;

  public static DateData from(final Date date) {
    return from(date.value);
  }

  public static DateData from(final long value) {
    return new DateData(value);
  }

  private DateData (final long value) {
    this.value = value;
  }

}