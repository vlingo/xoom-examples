package io.vlingo.xoom.examples.petclinic.infrastructure;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vlingo.xoom.examples.petclinic.model.Specialty;

public class SpecialtyData {

  public final String specialtyTypeId;

  public static SpecialtyData from(final Specialty specialty) {
    return from(specialty.specialtyTypeId);
  }

  @JsonCreator
  public static SpecialtyData from(@JsonProperty("specialtyTypeId") final String specialtyTypeId) {
    return new SpecialtyData(specialtyTypeId);
  }

  private SpecialtyData (final String specialtyTypeId) {
    this.specialtyTypeId = specialtyTypeId;
  }

}