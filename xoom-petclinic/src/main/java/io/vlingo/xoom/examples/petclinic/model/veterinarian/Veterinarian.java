package io.vlingo.xoom.examples.petclinic.model.veterinarian;

import io.vlingo.xoom.examples.petclinic.model.ContactInformation;
import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.examples.petclinic.model.client.Specialty;
import io.vlingo.xoom.actors.Address;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.examples.petclinic.model.Fullname;
import io.vlingo.xoom.common.Completes;

public interface Veterinarian {

  Completes<VeterinarianState> register(final Fullname name, final ContactInformation contact, final Specialty specialty);

  static Completes<VeterinarianState> register(final Stage stage, final Fullname name, final ContactInformation contact, final Specialty specialty) {
    final io.vlingo.xoom.actors.Address _address = stage.addressFactory().uniquePrefixedWith("g-");
    final Veterinarian _veterinarian = stage.actorFor(Veterinarian.class, Definition.has(VeterinarianEntity.class, Definition.parameters(_address.idString())), _address);
    return _veterinarian.register(name, contact, specialty);
  }

  Completes<VeterinarianState> changeName(final Fullname name);

  Completes<VeterinarianState> changeContactInformation(final ContactInformation contact);

  Completes<VeterinarianState> specializesIn(final Specialty specialty);

}