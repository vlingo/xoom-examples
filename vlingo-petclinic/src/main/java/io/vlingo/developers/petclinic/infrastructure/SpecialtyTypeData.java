package io.vlingo.developers.petclinic.infrastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import io.vlingo.developers.petclinic.model.specialtytype.SpecialtyTypeState;

public class SpecialtyTypeData {
  public final String id;
  public final String name;

  public static SpecialtyTypeData from(final SpecialtyTypeState state) {
    return new SpecialtyTypeData(state);
  }

  public static List<SpecialtyTypeData> from(final List<SpecialtyTypeState> states) {
    return states.stream().map(SpecialtyTypeData::from).collect(Collectors.toList());
  }

  public static SpecialtyTypeData from(String id, String name){
    return new SpecialtyTypeData(id, name);
  }

  public static SpecialtyTypeData empty() {
    return new SpecialtyTypeData(SpecialtyTypeState.identifiedBy(""));
  }

  private SpecialtyTypeData (final SpecialtyTypeState state) {
    this.id = state.id;
    this.name = state.name;
  }

  private SpecialtyTypeData(String id, String name) {
    this.id = id;
    this.name = name;
  }
}
