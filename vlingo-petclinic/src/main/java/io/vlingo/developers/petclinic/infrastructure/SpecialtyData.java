package io.vlingo.developers.petclinic.infrastructure;

public class SpecialtyData {

  public final String specialtyTypeId;

  public static SpecialtyData of(final String specialtyTypeId) {
    return new SpecialtyData(specialtyTypeId);
  }

  private SpecialtyData (final String specialtyTypeId) {
    this.specialtyTypeId = specialtyTypeId;
  }

}