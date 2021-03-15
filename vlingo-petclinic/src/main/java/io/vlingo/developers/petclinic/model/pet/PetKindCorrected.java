package io.vlingo.developers.petclinic.model.pet;

import io.vlingo.common.version.SemanticVersion;
import io.vlingo.lattice.model.IdentifiedDomainEvent;

import io.vlingo.developers.petclinic.model.client.Kind;

/**
 * See
 * <a href="https://docs.vlingo.io/vlingo-lattice/entity-cqrs#commands-domain-events-and-identified-domain-events">
 *   Commands, Domain Events, and Identified Domain Events
 * </a>
 */
public final class PetKindCorrected extends IdentifiedDomainEvent {

  private final String id;
  public final Kind kind;

  public PetKindCorrected(final String id, final Kind kind) {
    super(SemanticVersion.from("0.0.1").toValue());
    this.kind = kind;
    this.id = id;
  }

  @Override
  public String identity() {
    return id;
  }
}
