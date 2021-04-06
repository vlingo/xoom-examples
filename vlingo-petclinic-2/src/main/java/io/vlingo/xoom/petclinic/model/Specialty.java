package io.vlingo.xoom.petclinic.model;

public final class Specialty {

  public final String specialtyTypeId;

  public static Specialty from(final String specialtyTypeId) {
    return new Specialty(specialtyTypeId);
  }

  private Specialty (final String specialtyTypeId) {
    this.specialtyTypeId = specialtyTypeId;
  }

}