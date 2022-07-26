package io.vlingo.xoom.examples.petclinic.infrastructure;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vlingo.xoom.examples.petclinic.model.Kind;

public class KindData {

  public final String animalTypeId;

  public static KindData from(final Kind kind) {
    return from(kind.animalTypeId);
  }

  @JsonCreator
  public static KindData from(@JsonProperty("animalTypeId") final String animalTypeId) {
    return new KindData(animalTypeId);
  }

  private KindData (final String animalTypeId) {
    this.animalTypeId = animalTypeId;
  }

}