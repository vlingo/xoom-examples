package io.vlingo.xoom.examples.petclinic.infrastructure.persistence;

import java.util.Collection;
import io.vlingo.xoom.common.Completes;

import io.vlingo.xoom.examples.petclinic.infrastructure.VeterinarianData;

public interface VeterinarianQueries {
  Completes<VeterinarianData> veterinarianOf(String id);
  Completes<Collection<VeterinarianData>> veterinarians();
}