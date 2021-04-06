package io.vlingo.xoom.petclinic.model.pet;

import io.vlingo.xoom.petclinic.model.*;

public final class PetState {

  public final String id;
  public final Name name;
  public final Date birth;
  public final Date death;
  public final Kind kind;
  public final Owner owner;

  public static PetState identifiedBy(final String id) {
    return new PetState(id, null, null, null, null, null);
  }

  public PetState (final String id, final Name name, final Date birth, final Date death, final Kind kind, final Owner owner) {
    this.id = id;
    this.name = name;
    this.birth = birth;
    this.death = death;
    this.kind = kind;
    this.owner = owner;
  }

  public PetState register(final Name name, final Date birth, final Date death, final Kind kind, final Owner owner) {
    return new PetState(this.id, name, birth, death, kind, owner);
  }

  public PetState changeName(final Name name) {
    return new PetState(this.id, name, this.birth, this.death, this.kind, this.owner);
  }

  public PetState recordBirth(final Date birth) {
    return new PetState(this.id, this.name, birth, this.death, this.kind, this.owner);
  }

  public PetState recordDeath(final Date death) {
    return new PetState(this.id, this.name, this.birth, death, this.kind, this.owner);
  }

  public PetState correctKind(final Kind kind) {
    return new PetState(this.id, this.name, this.birth, this.death, kind, this.owner);
  }

  public PetState changeOwner(final Owner owner) {
    return new PetState(this.id, this.name, this.birth, this.death, this.kind, owner);
  }

}
