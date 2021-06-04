package io.vlingo.xoom.examples.petclinic.infrastructure.persistence;

import java.util.Collection;
import io.vlingo.xoom.common.Completes;

import io.vlingo.xoom.examples.petclinic.infrastructure.ClientData;

public interface ClientQueries {
  Completes<ClientData> clientOf(String id);
  Completes<Collection<ClientData>> clients();
}