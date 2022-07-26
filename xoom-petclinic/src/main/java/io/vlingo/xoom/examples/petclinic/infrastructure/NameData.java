package io.vlingo.xoom.examples.petclinic.infrastructure;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vlingo.xoom.examples.petclinic.model.Name;

public class NameData {

  public final String value;

  public static NameData from(final Name name) {
    return from(name.value);
  }

  @JsonCreator
  public static NameData from(@JsonProperty("value") final String value) {
    return new NameData(value);
  }

  private NameData (final String value) {
    this.value = value;
  }

}