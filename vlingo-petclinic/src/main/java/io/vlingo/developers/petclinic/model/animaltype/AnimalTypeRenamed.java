package io.vlingo.developers.petclinic.model.animaltype;

import java.util.UUID;
import io.vlingo.common.version.SemanticVersion;
import io.vlingo.lattice.model.IdentifiedDomainEvent;


/**
 * See
 * <a href="https://docs.vlingo.io/vlingo-lattice/entity-cqrs#commands-domain-events-and-identified-domain-events">
 *   Commands, Domain Events, and Identified Domain Events
 * </a>
 */
public final class AnimalTypeRenamed extends IdentifiedDomainEvent {

  private final UUID eventId;
  public final String name;

  public AnimalTypeRenamed(final String name) {
    super(SemanticVersion.from("0.0.1").toValue());
    this.name = name;
    this.eventId = UUID.randomUUID(); //TODO: Define the event id
  }

  @Override
  public String identity() {
    return eventId.toString();
  }
}
