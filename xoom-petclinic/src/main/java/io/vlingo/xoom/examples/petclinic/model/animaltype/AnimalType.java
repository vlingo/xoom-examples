package io.vlingo.xoom.examples.petclinic.model.animaltype;

import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.Address;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.common.Completes;

public interface AnimalType {

  Completes<AnimalTypeState> offerTreatmentFor(final String name);

  static Completes<AnimalTypeState> offerTreatmentFor(final Stage stage, final String name) {
    final io.vlingo.xoom.actors.Address _address = stage.addressFactory().uniquePrefixedWith("g-");
    final AnimalType _animalType = stage.actorFor(AnimalType.class, Definition.has(AnimalTypeEntity.class, Definition.parameters(_address.idString())), _address);
    return _animalType.offerTreatmentFor(name);
  }

  Completes<AnimalTypeState> rename(final String name);

}