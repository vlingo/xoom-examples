package io.vlingo.developers.petclinic.model.specialtytype;

import io.vlingo.common.version.SemanticVersion;
import io.vlingo.lattice.model.IdentifiedDomainEvent;


/**
 * See
 * <a href="https://docs.vlingo.io/vlingo-lattice/entity-cqrs#commands-domain-events-and-identified-domain-events">
 *   Commands, Domain Events, and Identified Domain Events
 * </a>
 */
public final class SpecialtyTypeRenamed extends IdentifiedDomainEvent {

  private final String id;
  public final String name;

  public SpecialtyTypeRenamed(final String id, final String name) {
    super(SemanticVersion.from("0.0.1").toValue());
    this.name = name;
    this.id = id;
  }

  @Override
  public String identity() {
    return id;
  }
}
