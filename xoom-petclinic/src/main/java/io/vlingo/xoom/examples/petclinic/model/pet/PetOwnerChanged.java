package io.vlingo.xoom.examples.petclinic.model.pet;

import io.vlingo.xoom.common.version.SemanticVersion;
import io.vlingo.xoom.lattice.model.IdentifiedDomainEvent;

import io.vlingo.xoom.examples.petclinic.model.client.Owner;

/**
 * See
 * <a href="https://docs.vlingo.io/xoom-lattice/entity-cqrs#commands-domain-events-and-identified-domain-events">
 *   Commands, Domain Events, and Identified Domain Events
 * </a>
 */
public final class PetOwnerChanged extends IdentifiedDomainEvent {

  private final String id;
  public final Owner owner;

  public PetOwnerChanged(final String id, final Owner owner) {
    super(SemanticVersion.from("0.0.1").toValue());
    this.owner = owner;
    this.id = id;
  }

  @Override
  public String identity() {
    return id;
  }
}
