package io.vlingo.developers.petclinic.infrastructure;

public class KindData {

  public final String animalTypeId;

  public static KindData of(final String animalTypeId) {
    return new KindData(animalTypeId);
  }

  private KindData (final String animalTypeId) {
    this.animalTypeId = animalTypeId;
  }

}