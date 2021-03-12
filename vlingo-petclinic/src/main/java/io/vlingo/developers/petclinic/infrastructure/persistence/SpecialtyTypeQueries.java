package io.vlingo.developers.petclinic.infrastructure.persistence;

import java.util.Collection;
import io.vlingo.common.Completes;

import io.vlingo.developers.petclinic.infrastructure.SpecialtyTypeData;

public interface SpecialtyTypeQueries {
  Completes<SpecialtyTypeData> specialtyTypeOf(String id);
  Completes<Collection<SpecialtyTypeData>> specialtyTypes();
}