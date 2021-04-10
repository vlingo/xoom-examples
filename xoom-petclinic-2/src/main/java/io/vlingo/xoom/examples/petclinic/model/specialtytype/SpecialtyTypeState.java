package io.vlingo.xoom.examples.petclinic.model.specialtytype;


public final class SpecialtyTypeState {

  public final String id;
  public final String name;

  public static SpecialtyTypeState identifiedBy(final String id) {
    return new SpecialtyTypeState(id, null);
  }

  public SpecialtyTypeState (final String id, final String name) {
    this.id = id;
    this.name = name;
  }

  public SpecialtyTypeState offer(final String name) {
    return new SpecialtyTypeState(this.id, name);
  }

  public SpecialtyTypeState rename(final String name) {
    return new SpecialtyTypeState(this.id, name);
  }

}
