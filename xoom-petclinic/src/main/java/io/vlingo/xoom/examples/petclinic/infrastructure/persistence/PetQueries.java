package io.vlingo.xoom.examples.petclinic.infrastructure.persistence;

import java.util.Collection;
import io.vlingo.xoom.common.Completes;

import io.vlingo.xoom.examples.petclinic.infrastructure.PetData;

public interface PetQueries {
  Completes<PetData> petOf(String id);
  Completes<Collection<PetData>> pets();
  Completes<Collection<PetData>> petsForOwner(String ownerId);
}
