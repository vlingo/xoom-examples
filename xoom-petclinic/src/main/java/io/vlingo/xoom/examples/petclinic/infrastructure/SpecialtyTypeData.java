package io.vlingo.xoom.examples.petclinic.infrastructure;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vlingo.xoom.examples.petclinic.model.specialtytype.SpecialtyTypeState;

public class SpecialtyTypeData {
  public final String id;
  public final String name;

  public static SpecialtyTypeData from(final SpecialtyTypeState specialtyTypeState) {
    return from(specialtyTypeState.id, specialtyTypeState.name);
  }

  @JsonCreator
  public static SpecialtyTypeData from(@JsonProperty("id") final String id,
                                       @JsonProperty("name") final String name) {
    return new SpecialtyTypeData(id, name);
  }

  public static List<SpecialtyTypeData> from(final List<SpecialtyTypeState> states) {
    return states.stream().map(SpecialtyTypeData::from).collect(Collectors.toList());
  }

  public static SpecialtyTypeData empty() {
    return from(SpecialtyTypeState.identifiedBy(""));
  }

  private SpecialtyTypeData (final String id, final String name) {
    this.id = id;
    this.name = name;
  }

}
