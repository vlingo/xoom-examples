package io.vlingo.xoom.examples.petclinic.infrastructure;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vlingo.xoom.examples.petclinic.model.FullName;

public class FullNameData {

  public final String first;
  public final String last;

  public static FullNameData from(final FullName fullName) {
    return from(fullName.first, fullName.last);
  }

  @JsonCreator
  public static FullNameData from(@JsonProperty("first") final String first,
                                  @JsonProperty("last") final String last) {
    return new FullNameData(first, last);
  }

  private FullNameData (final String first, final String last) {
    this.first = first;
    this.last = last;
  }

}