package io.vlingo.developers.petclinic.infrastructure.persistence;

import java.util.Collection;
import io.vlingo.common.Completes;

import io.vlingo.developers.petclinic.infrastructure.PetData;

public interface PetQueries {
  Completes<PetData> petOf(String id);
  Completes<Collection<PetData>> pets();
  Completes<Collection<PetData>> petsForOwner(String ownerId);
}
