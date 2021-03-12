package io.vlingo.developers.petclinic.model.pet;

import java.util.UUID;
import io.vlingo.common.version.SemanticVersion;
import io.vlingo.lattice.model.IdentifiedDomainEvent;

import io.vlingo.developers.petclinic.model.client.Kind;
import io.vlingo.developers.petclinic.model.client.Name;
import io.vlingo.developers.petclinic.model.client.Owner;

/**
 * See
 * <a href="https://docs.vlingo.io/vlingo-lattice/entity-cqrs#commands-domain-events-and-identified-domain-events">
 *   Commands, Domain Events, and Identified Domain Events
 * </a>
 */
public final class PetRegistered extends IdentifiedDomainEvent {

  private final UUID eventId;
  public final String id;
  public final Name name;
  public final long birth;
  public final Kind kind;
  public final Owner owner;

  public PetRegistered(final String id, final Name name, long birth, Kind kind, Owner owner) {
    super(SemanticVersion.from("0.0.1").toValue());
    this.id = id;
    this.name = name;
    this.birth = birth;
    this.kind = kind;
    this.owner = owner;
    this.eventId = UUID.randomUUID(); //TODO: Define the event id
  }

  @Override
  public String identity() {
    return eventId.toString();
  }
}
