package io.vlingo.developers.petclinic.model.pet;

import io.vlingo.common.version.SemanticVersion;
import io.vlingo.lattice.model.IdentifiedDomainEvent;


/**
 * See
 * <a href="https://docs.vlingo.io/vlingo-lattice/entity-cqrs#commands-domain-events-and-identified-domain-events">
 *   Commands, Domain Events, and Identified Domain Events
 * </a>
 */
public final class PetBirthRecorded extends IdentifiedDomainEvent {

  private final String id;
  public final long birth;

  public PetBirthRecorded(final String id, final long birth) {
    super(SemanticVersion.from("0.0.1").toValue());
    this.birth = birth;
    this.id = id;
  }

  @Override
  public String identity() {
    return id;
  }
}
