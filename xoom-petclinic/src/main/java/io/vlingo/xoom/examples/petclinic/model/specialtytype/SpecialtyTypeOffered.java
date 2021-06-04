package io.vlingo.xoom.examples.petclinic.model.specialtytype;

import io.vlingo.xoom.common.version.SemanticVersion;
import io.vlingo.xoom.lattice.model.IdentifiedDomainEvent;


/**
 * See
 * <a href="https://docs.vlingo.io/xoom-lattice/entity-cqrs#commands-domain-events-and-identified-domain-events">
 *   Commands, Domain Events, and Identified Domain Events
 * </a>
 */
public final class SpecialtyTypeOffered extends IdentifiedDomainEvent {

  public final String id;
  public final String name;

  public SpecialtyTypeOffered(final SpecialtyTypeState state) {
    super(SemanticVersion.from("1.0.0").toValue());
    this.id = state.id;
    this.name = state.name;
  }

  @Override
  public String identity() {
    return id;
  }
}
