package io.vlingo.xoom.examples.petclinic.model.veterinarian;

import io.vlingo.xoom.common.version.SemanticVersion;
import io.vlingo.xoom.lattice.model.IdentifiedDomainEvent;

import io.vlingo.xoom.examples.petclinic.model.*;

/**
 * See
 * <a href="https://docs.vlingo.io/xoom-lattice/entity-cqrs#commands-domain-events-and-identified-domain-events">
 *   Commands, Domain Events, and Identified Domain Events
 * </a>
 */
public final class VeterinarianSpecialtyChosen extends IdentifiedDomainEvent {

  public final String id;
  public final Specialty specialty;

  public VeterinarianSpecialtyChosen(final VeterinarianState state) {
    super(SemanticVersion.from("1.0.0").toValue());
    this.id = state.id;
    this.specialty = state.specialty;
  }

  @Override
  public String identity() {
    return id;
  }
}
