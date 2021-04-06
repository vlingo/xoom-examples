package io.vlingo.xoom.petclinic.infrastructure;

import io.vlingo.xoom.petclinic.model.Specialty;

public class SpecialtyData {

  public final String specialtyTypeId;

  public static SpecialtyData from(final Specialty specialty) {
    return from(specialty.specialtyTypeId);
  }

  public static SpecialtyData from(final String specialtyTypeId) {
    return new SpecialtyData(specialtyTypeId);
  }

  private SpecialtyData (final String specialtyTypeId) {
    this.specialtyTypeId = specialtyTypeId;
  }

}