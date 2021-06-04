package io.vlingo.xoom.examples.petclinic.model;

public final class Kind {

  public final String animalTypeId;

  public static Kind from(final String animalTypeId) {
    return new Kind(animalTypeId);
  }

  private Kind (final String animalTypeId) {
    this.animalTypeId = animalTypeId;
  }

}