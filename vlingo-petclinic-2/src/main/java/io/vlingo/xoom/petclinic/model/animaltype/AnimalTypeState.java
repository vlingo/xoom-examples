package io.vlingo.xoom.petclinic.model.animaltype;


public final class AnimalTypeState {

  public final String id;
  public final String name;

  public static AnimalTypeState identifiedBy(final String id) {
    return new AnimalTypeState(id, null);
  }

  public AnimalTypeState (final String id, final String name) {
    this.id = id;
    this.name = name;
  }

  public AnimalTypeState offerTreatmentFor(final String name) {
    return new AnimalTypeState(this.id, name);
  }

  public AnimalTypeState rename(final String name) {
    return new AnimalTypeState(this.id, name);
  }

}
