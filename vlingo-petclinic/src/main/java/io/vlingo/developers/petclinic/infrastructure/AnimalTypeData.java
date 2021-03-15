package io.vlingo.developers.petclinic.infrastructure;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import io.vlingo.developers.petclinic.model.animaltype.AnimalTypeState;

public class AnimalTypeData {
  public final String id;
  public final String name;

  public static AnimalTypeData from(final AnimalTypeState state) {
    Objects.requireNonNull(state, "state can not be null");
    return new AnimalTypeData(state);
  }

  public static List<AnimalTypeData> from(final List<AnimalTypeState> states) {
    return states.stream().map(AnimalTypeData::from).collect(Collectors.toList());
  }

  public static AnimalTypeData from(final String id, String name){
    return new AnimalTypeData(id, name);
  }

  public static AnimalTypeData empty() {
    return new AnimalTypeData(AnimalTypeState.identifiedBy(null));
  }

  private AnimalTypeData (final AnimalTypeState state) {
    this.id = state.id;
    this.name = state.name;
  }

  private AnimalTypeData(String id, String name) {
    this.id = id;
    this.name = name;
  }
}
