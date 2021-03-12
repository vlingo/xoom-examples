package io.vlingo.developers.petclinic.model.client;

public class Specialty {

  public final String specialtyTypeId;

  public static Specialty of(final String specialtyTypeId) {
    return new Specialty(specialtyTypeId);
  }

  private Specialty (final String specialtyTypeId) {
    this.specialtyTypeId = specialtyTypeId;
  }

}