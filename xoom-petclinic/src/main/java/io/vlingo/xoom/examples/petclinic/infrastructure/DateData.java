package io.vlingo.xoom.examples.petclinic.infrastructure;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vlingo.xoom.examples.petclinic.model.Date;

public class DateData {

  public final long value;

  public static DateData from(final Date date) {
    return from(date.value);
  }

  @JsonCreator
  public static DateData from(@JsonProperty("value") final long value) {
    return new DateData(value);
  }

  private DateData (final long value) {
    this.value = value;
  }

}