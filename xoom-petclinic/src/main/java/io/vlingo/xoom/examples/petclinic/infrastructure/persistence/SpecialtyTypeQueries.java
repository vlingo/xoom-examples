package io.vlingo.xoom.examples.petclinic.infrastructure.persistence;

import java.util.Collection;
import io.vlingo.xoom.common.Completes;

import io.vlingo.xoom.examples.petclinic.infrastructure.SpecialtyTypeData;

public interface SpecialtyTypeQueries {
  Completes<SpecialtyTypeData> specialtyTypeOf(String id);
  Completes<Collection<SpecialtyTypeData>> specialtyTypes();
}