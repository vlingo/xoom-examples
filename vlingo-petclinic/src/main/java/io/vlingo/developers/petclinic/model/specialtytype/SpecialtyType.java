package io.vlingo.developers.petclinic.model.specialtytype;

import io.vlingo.actors.Definition;
import io.vlingo.actors.Address;
import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;

public interface SpecialtyType {

  Completes<SpecialtyTypeState> offer(final String name);

  static Completes<SpecialtyTypeState> offer(final Stage stage, final String name) {
    final io.vlingo.actors.Address _address = stage.addressFactory().uniquePrefixedWith("g-");
    final SpecialtyType _specialtyType = stage.actorFor(SpecialtyType.class, Definition.has(SpecialtyTypeEntity.class, Definition.parameters(_address.idString())), _address);
    return _specialtyType.offer(name);
  }

  Completes<SpecialtyTypeState> rename(final String name);

}