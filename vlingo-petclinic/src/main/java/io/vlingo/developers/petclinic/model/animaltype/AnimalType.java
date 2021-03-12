package io.vlingo.developers.petclinic.model.animaltype;

import io.vlingo.actors.Definition;
import io.vlingo.actors.Address;
import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;

public interface AnimalType {

  Completes<AnimalTypeState> offerTreatmentFor(final String name);

  static Completes<AnimalTypeState> offerTreatmentFor(final Stage stage, final String name) {
    final io.vlingo.actors.Address _address = stage.addressFactory().uniquePrefixedWith("g-");
    final AnimalType _animalType = stage.actorFor(AnimalType.class, Definition.has(AnimalTypeEntity.class, Definition.parameters(_address.idString())), _address);
    return _animalType.offerTreatmentFor(name);
  }

  Completes<AnimalTypeState> rename(final String name);

}