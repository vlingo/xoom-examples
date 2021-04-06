package io.vlingo.xoom.petclinic.infrastructure.persistence;

import java.util.Collection;
import io.vlingo.common.Completes;

import io.vlingo.xoom.petclinic.infrastructure.VeterinarianData;

public interface VeterinarianQueries {
  Completes<VeterinarianData> veterinarianOf(String id);
  Completes<Collection<VeterinarianData>> veterinarians();
}