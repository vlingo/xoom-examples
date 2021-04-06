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
public final class PetKindCorrected extends IdentifiedDomainEvent {

  public final String id;
  public final Kind kind;

  public PetKindCorrected(final PetState state) {
    super(SemanticVersion.from("1.0.0").toValue());
    this.id = state.id;
    this.kind = state.kind;
  }

  @Override
  public String identity() {
    return id;
  }
}
