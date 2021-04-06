package io.vlingo.xoom.petclinic.infrastructure.persistence;

import java.util.Collection;
import io.vlingo.common.Completes;

import io.vlingo.xoom.petclinic.infrastructure.ClientData;

public interface ClientQueries {
  Completes<ClientData> clientOf(String id);
  Completes<Collection<ClientData>> clients();
}