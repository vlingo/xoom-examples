package io.vlingo.xoom.examples.petclinic.model.client;

public class Kind {

  public final String animalTypeId;

  public static Kind of(final String animalTypeId) {
    return new Kind(animalTypeId);
  }

  private Kind (final String animalTypeId) {
    this.animalTypeId = animalTypeId;
  }

}