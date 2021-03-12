package io.vlingo.developers.petclinic.model.pet;

import io.vlingo.developers.petclinic.model.client.Kind;
import io.vlingo.developers.petclinic.model.client.Visit;
import io.vlingo.developers.petclinic.model.client.Name;
import io.vlingo.actors.Definition;
import io.vlingo.developers.petclinic.model.client.Owner;
import io.vlingo.actors.Address;
import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;

public interface Pet {

  Completes<PetState> register(final Name name, final long birth, final Kind kind, final Owner owner);

  static Completes<PetState> register(final Stage stage, final Name name, final long birth, final Kind kind, final Owner owner) {
    final io.vlingo.actors.Address _address = stage.addressFactory().uniquePrefixedWith("g-");
    final Pet _pet = stage.actorFor(Pet.class, Definition.has(PetEntity.class, Definition.parameters(_address.idString())), _address);
    return _pet.register(name, birth, kind, owner);
  }

  Completes<PetState> changeName(final Name name);

  Completes<PetState> recordBirth(final long birth);

  Completes<PetState> recordDeath(final long death);

  Completes<PetState> correctKind(final Kind kind);

  Completes<PetState> changeOwner(final Owner owner);

}