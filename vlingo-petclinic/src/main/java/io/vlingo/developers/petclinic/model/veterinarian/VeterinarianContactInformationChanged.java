package io.vlingo.developers.petclinic.model.veterinarian;

import java.util.UUID;
import io.vlingo.common.version.SemanticVersion;
import io.vlingo.lattice.model.IdentifiedDomainEvent;

import io.vlingo.developers.petclinic.model.ContactInformation;

/**
 * See
 * <a href="https://docs.vlingo.io/vlingo-lattice/entity-cqrs#commands-domain-events-and-identified-domain-events">
 *   Commands, Domain Events, and Identified Domain Events
 * </a>
 */
public final class VeterinarianContactInformationChanged extends IdentifiedDomainEvent {

  private final UUID eventId;
  public final ContactInformation contact;

  public VeterinarianContactInformationChanged(final ContactInformation contact) {
    super(SemanticVersion.from("0.0.1").toValue());
    this.contact = contact;
    this.eventId = UUID.randomUUID(); //TODO: Define the event id
  }

  @Override
  public String identity() {
    return eventId.toString();
  }
}
