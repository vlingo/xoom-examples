package io.vlingo.xoom.examples.petclinic.model.veterinarian;

import io.vlingo.xoom.common.version.SemanticVersion;
import io.vlingo.xoom.lattice.model.IdentifiedDomainEvent;

import io.vlingo.xoom.examples.petclinic.model.Fullname;

/**
 * See
 * <a href="https://docs.vlingo.io/xoom-lattice/entity-cqrs#commands-domain-events-and-identified-domain-events">
 *   Commands, Domain Events, and Identified Domain Events
 * </a>
 */
public final class VeterinarianNameChanged extends IdentifiedDomainEvent {

  private final String id;
  public final Fullname name;

  public VeterinarianNameChanged(final String id, final Fullname name) {
    super(SemanticVersion.from("0.0.1").toValue());
    this.name = name;
    this.id = id;
  }

  @Override
  public String identity() {
    return id;
  }
}
