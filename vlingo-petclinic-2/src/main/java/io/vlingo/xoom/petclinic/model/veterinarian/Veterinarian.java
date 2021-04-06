package io.vlingo.xoom.petclinic.model.veterinarian;

import io.vlingo.actors.Definition;
import io.vlingo.xoom.petclinic.model.*;
import io.vlingo.actors.Address;
import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;

public interface Veterinarian {

  Completes<VeterinarianState> register(final FullName name, final ContactInformation contactInformation, final Specialty specialty);

  static Completes<VeterinarianState> register(final Stage stage, final FullName name, final ContactInformation contactInformation, final Specialty specialty) {
    final io.vlingo.actors.Address _address = stage.addressFactory().uniquePrefixedWith("g-");
    final Veterinarian _veterinarian = stage.actorFor(Veterinarian.class, Definition.has(VeterinarianEntity.class, Definition.parameters(_address.idString())), _address);
    return _veterinarian.register(name, contactInformation, specialty);
  }

  Completes<VeterinarianState> changeContactInformation(final ContactInformation contactInformation);

  Completes<VeterinarianState> changeName(final FullName name);

  Completes<VeterinarianState> specializesIn(final Specialty specialty);

}