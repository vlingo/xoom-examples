package io.vlingo.xoom.examples.petclinic.model.animaltype;

import io.vlingo.xoom.common.version.SemanticVersion;
import io.vlingo.xoom.lattice.model.IdentifiedDomainEvent;


/**
 * See
 * <a href="https://docs.vlingo.io/xoom-lattice/entity-cqrs#commands-domain-events-and-identified-domain-events">
 *   Commands, Domain Events, and Identified Domain Events
 * </a>
 */
public final class AnimalTypeTreatmentOffered extends IdentifiedDomainEvent {

  public final String id;
  public final String name;

  public AnimalTypeTreatmentOffered(final String id, final String name) {
    super(SemanticVersion.from("0.0.1").toValue());
    this.id = id;
    this.name = name;
  }

  @Override
  public String identity() {
    return id;
  }
}
