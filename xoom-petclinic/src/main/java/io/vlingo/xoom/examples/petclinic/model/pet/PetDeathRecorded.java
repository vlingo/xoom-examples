package io.vlingo.xoom.examples.petclinic.model.pet;

import io.vlingo.xoom.common.version.SemanticVersion;
import io.vlingo.xoom.lattice.model.IdentifiedDomainEvent;


/**
 * See
 * <a href="https://docs.vlingo.io/xoom-lattice/entity-cqrs#commands-domain-events-and-identified-domain-events">
 *   Commands, Domain Events, and Identified Domain Events
 * </a>
 */
public final class PetDeathRecorded extends IdentifiedDomainEvent {

  private final String id;
  public final long death;

  public PetDeathRecorded(final String id, final long death) {
    super(SemanticVersion.from("0.0.1").toValue());
    this.death = death;
    this.id = id;
  }

  @Override
  public String identity() {
    return id;
  }
}
