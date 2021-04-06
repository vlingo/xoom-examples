package io.vlingo.xoom.petclinic.model.pet;

import io.vlingo.common.version.SemanticVersion;
import io.vlingo.lattice.model.IdentifiedDomainEvent;

import io.vlingo.xoom.petclinic.model.*;

/**
 * See
 * <a href="https://docs.vlingo.io/vlingo-lattice/entity-cqrs#commands-domain-events-and-identified-domain-events">
 *   Commands, Domain Events, and Identified Domain Events
 * </a>
 */
public final class PetRegistered extends IdentifiedDomainEvent {

  public final String id;
  public final Name name;
  public final Date birth;
  public final Kind kind;
  public final Owner owner;

  public PetRegistered(final PetState state) {
    super(SemanticVersion.from("1.0.0").toValue());
    this.id = state.id;
    this.name = state.name;
    this.birth = state.birth;
    this.kind = state.kind;
    this.owner = state.owner;
  }

  @Override
  public String identity() {
    return id;
  }
}
