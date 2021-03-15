package io.vlingo.developers.petclinic.model.client;

import io.vlingo.common.version.SemanticVersion;
import io.vlingo.lattice.model.IdentifiedDomainEvent;

import io.vlingo.developers.petclinic.model.ContactInformation;

/**
 * See
 * <a href="https://docs.vlingo.io/vlingo-lattice/entity-cqrs#commands-domain-events-and-identified-domain-events">
 *   Commands, Domain Events, and Identified Domain Events
 * </a>
 */
public final class ClientContactInformationChanged extends IdentifiedDomainEvent {

  private final String id;
  public final ContactInformation contact;

  public ClientContactInformationChanged(final String id, final ContactInformation contact) {
    super(SemanticVersion.from("0.0.1").toValue());
    this.contact = contact;
    this.id = id;
  }

  @Override
  public String identity() {
    return id;
  }
}
