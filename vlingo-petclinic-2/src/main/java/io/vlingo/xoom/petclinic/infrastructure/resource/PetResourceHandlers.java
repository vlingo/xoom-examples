package io.vlingo.xoom.petclinic.infrastructure.resource;

import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;
import io.vlingo.xoom.annotation.autodispatch.Handler.Three;
import io.vlingo.xoom.annotation.autodispatch.Handler.Two;
import io.vlingo.xoom.annotation.autodispatch.HandlerEntry;

import io.vlingo.xoom.petclinic.infrastructure.persistence.PetQueries;
import io.vlingo.xoom.petclinic.model.pet.Pet;
import io.vlingo.xoom.petclinic.model.*;
import io.vlingo.xoom.petclinic.infrastructure.PetData;
import io.vlingo.xoom.petclinic.model.pet.PetState;
import java.util.Collection;

public class PetResourceHandlers {

  public static final int REGISTER = 0;
  public static final int CHANGE_NAME = 1;
  public static final int RECORD_BIRTH = 2;
  public static final int RECORD_DEATH = 3;
  public static final int CORRECT_KIND = 4;
  public static final int CHANGE_OWNER = 5;
  public static final int PETS = 6;
  public static final int ADAPT_STATE = 7;

  public static final HandlerEntry<Three<Completes<PetState>, Stage, PetData>> REGISTER_HANDLER =
          HandlerEntry.of(REGISTER, ($stage, data) -> {
              final Name name = Name.from(data.name.value);
              final Date birth = Date.from(data.birth.value);
              final Date death = Date.from(data.death.value);
              final Kind kind = Kind.from(data.kind.animalTypeId);
              final Owner owner = Owner.from(data.owner.clientId);
              return Pet.register($stage, name, birth, death, kind, owner);
          });

  public static final HandlerEntry<Three<Completes<PetState>, Pet, PetData>> CHANGE_NAME_HANDLER =
          HandlerEntry.of(CHANGE_NAME, (pet, data) -> {
              final Name name = Name.from(data.name.value);
              return pet.changeName(name);
          });

  public static final HandlerEntry<Three<Completes<PetState>, Pet, PetData>> RECORD_BIRTH_HANDLER =
          HandlerEntry.of(RECORD_BIRTH, (pet, data) -> {
              final Date birth = Date.from(data.birth.value);
              return pet.recordBirth(birth);
          });

  public static final HandlerEntry<Three<Completes<PetState>, Pet, PetData>> RECORD_DEATH_HANDLER =
          HandlerEntry.of(RECORD_DEATH, (pet, data) -> {
              final Date death = Date.from(data.death.value);
              return pet.recordDeath(death);
          });

  public static final HandlerEntry<Three<Completes<PetState>, Pet, PetData>> CORRECT_KIND_HANDLER =
          HandlerEntry.of(CORRECT_KIND, (pet, data) -> {
              final Kind kind = Kind.from(data.kind.animalTypeId);
              return pet.correctKind(kind);
          });

  public static final HandlerEntry<Three<Completes<PetState>, Pet, PetData>> CHANGE_OWNER_HANDLER =
          HandlerEntry.of(CHANGE_OWNER, (pet, data) -> {
              final Owner owner = Owner.from(data.owner.clientId);
              return pet.changeOwner(owner);
          });

  public static final HandlerEntry<Two<PetData, PetState>> ADAPT_STATE_HANDLER =
          HandlerEntry.of(ADAPT_STATE, PetData::from);

  public static final HandlerEntry<Two<Completes<Collection<PetData>>, PetQueries>> QUERY_ALL_HANDLER =
          HandlerEntry.of(PETS, PetQueries::pets);

}