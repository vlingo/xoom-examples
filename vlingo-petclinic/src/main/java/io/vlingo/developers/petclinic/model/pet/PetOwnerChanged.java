package io.vlingo.developers.petclinic.model.pet;

import java.util.UUID;
import io.vlingo.common.version.SemanticVersion;
import io.vlingo.lattice.model.IdentifiedDomainEvent;

import io.vlingo.developers.petclinic.model.client.Owner;

/**
 * See
 * <a href="https://docs.vlingo.io/vlingo-lattice/entity-cqrs#commands-domain-events-and-identified-domain-events">
 *   Commands, Domain Events, and Identified Domain Events
 * </a>
 */
public final class PetOwnerChanged extends IdentifiedDomainEvent {

  private final UUID eventId;
  public final Owner owner;

  public PetOwnerChanged(final Owner owner) {
    super(SemanticVersion.from("0.0.1").toValue());
    this.owner = owner;
    this.eventId = UUID.randomUUID(); //TODO: Define the event id
  }

  @Override
  public String identity() {
    return eventId.toString();
  }
}
