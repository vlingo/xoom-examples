package io.vlingo.xoom.examples.petclinic.model.pet;

import io.vlingo.xoom.common.version.SemanticVersion;
import io.vlingo.xoom.lattice.model.IdentifiedDomainEvent;

import io.vlingo.xoom.examples.petclinic.model.client.Kind;
import io.vlingo.xoom.examples.petclinic.model.client.Name;
import io.vlingo.xoom.examples.petclinic.model.client.Owner;

/**
 * See
 * <a href="https://docs.vlingo.io/xoom-lattice/entity-cqrs#commands-domain-events-and-identified-domain-events">
 *   Commands, Domain Events, and Identified Domain Events
 * </a>
 */
public final class PetRegistered extends IdentifiedDomainEvent {

  public final String id;
  public final Name name;
  public final long birth;
  public final Kind kind;
  public final Owner owner;

  public PetRegistered(final String id, final Name name, long birth, Kind kind, Owner owner) {
    super(SemanticVersion.from("0.0.1").toValue());
    this.id = id;
    this.name = name;
    this.birth = birth;
    this.kind = kind;
    this.owner = owner;
  }

  @Override
  public String identity() {
    return id;
  }
}
