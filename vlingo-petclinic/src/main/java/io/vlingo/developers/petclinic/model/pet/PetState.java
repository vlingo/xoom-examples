package io.vlingo.developers.petclinic.model.pet;

import io.vlingo.developers.petclinic.model.client.Kind;
import io.vlingo.developers.petclinic.model.client.Visit;
import io.vlingo.developers.petclinic.model.client.Name;
import io.vlingo.developers.petclinic.model.client.Owner;

public final class PetState {

  public final String id;
  public final Name name;
  public final long birth;
  public final long death;
  public final Kind kind;
  public final Owner owner;
  public final Visit visit;

  public static PetState identifiedBy(final String id) {
    return new PetState(id, null, 0, 0, null, null, null);
  }

  public PetState (final String id, final Name name, final long birth, final long death, final Kind kind, final Owner owner, final Visit visit) {
    this.id = id;
    this.name = name;
    this.birth = birth;
    this.death = death;
    this.kind = kind;
    this.owner = owner;
    this.visit = visit;
  }

  public PetState register(final Name name, final long birth, final Kind kind, final Owner owner) {
    return new PetState(this.id, name, birth, this.death, kind, owner, this.visit);
  }

  public PetState changeName(final Name name) {
    return new PetState(this.id, name, this.birth, this.death, this.kind, this.owner, this.visit);
  }

  public PetState recordBirth(final long birth) {
    return new PetState(this.id, this.name, birth, this.death, this.kind, this.owner, this.visit);
  }

  public PetState recordDeath(final long death) {
    return new PetState(this.id, this.name, this.birth, death, this.kind, this.owner, this.visit);
  }

  public PetState correctKind(final Kind kind) {
    return new PetState(this.id, this.name, this.birth, this.death, kind, this.owner, this.visit);
  }

  public PetState changeOwner(final Owner owner) {
    return new PetState(this.id, this.name, this.birth, this.death, this.kind, owner, this.visit);
  }

}
