package io.vlingo.xoom.examples.petclinic.infrastructure;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vlingo.xoom.examples.petclinic.model.animaltype.AnimalTypeState;

public class AnimalTypeData {
  public final String id;
  public final String name;

  public static AnimalTypeData from(final AnimalTypeState animalTypeState) {
    return from(animalTypeState.id, animalTypeState.name);
  }

  @JsonCreator
  public static AnimalTypeData from(@JsonProperty("id") final String id,
                                    @JsonProperty("name") final String name) {
    return new AnimalTypeData(id, name);
  }

  public static List<AnimalTypeData> from(final List<AnimalTypeState> states) {
    return states.stream().map(AnimalTypeData::from).collect(Collectors.toList());
  }

  public static AnimalTypeData empty() {
    return from(AnimalTypeState.identifiedBy(""));
  }

  private AnimalTypeData (final String id, final String name) {
    this.id = id;
    this.name = name;
  }

}
