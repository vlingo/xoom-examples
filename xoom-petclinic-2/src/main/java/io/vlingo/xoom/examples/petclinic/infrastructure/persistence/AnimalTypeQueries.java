package io.vlingo.xoom.examples.petclinic.infrastructure.persistence;

import java.util.Collection;
import io.vlingo.xoom.common.Completes;

import io.vlingo.xoom.examples.petclinic.infrastructure.AnimalTypeData;

public interface AnimalTypeQueries {
  Completes<AnimalTypeData> animalTypeOf(String id);
  Completes<Collection<AnimalTypeData>> animalTypes();
}