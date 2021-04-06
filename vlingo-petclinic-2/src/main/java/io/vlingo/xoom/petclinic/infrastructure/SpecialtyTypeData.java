package io.vlingo.xoom.petclinic.infrastructure;

import java.util.List;
import java.util.stream.Collectors;
import io.vlingo.xoom.petclinic.model.specialtytype.SpecialtyTypeState;

public class SpecialtyTypeData {
  public final String id;
  public final String name;

  public static SpecialtyTypeData from(final SpecialtyTypeState specialtyTypeState) {
    return from(specialtyTypeState.id, specialtyTypeState.name);
  }

  public static SpecialtyTypeData from(final String id, final String name) {
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
