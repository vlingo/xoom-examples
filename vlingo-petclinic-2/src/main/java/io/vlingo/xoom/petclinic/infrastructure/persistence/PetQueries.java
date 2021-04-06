package io.vlingo.xoom.petclinic.infrastructure.persistence;

import java.util.Collection;
import io.vlingo.common.Completes;

import io.vlingo.xoom.petclinic.infrastructure.PetData;

public interface PetQueries {
  Completes<PetData> petOf(String id);
  Completes<Collection<PetData>> pets();
}