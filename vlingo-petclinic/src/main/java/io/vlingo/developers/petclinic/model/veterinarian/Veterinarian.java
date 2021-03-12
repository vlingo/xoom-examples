package io.vlingo.developers.petclinic.model.veterinarian;

import io.vlingo.developers.petclinic.model.ContactInformation;
import io.vlingo.actors.Definition;
import io.vlingo.developers.petclinic.model.client.Specialty;
import io.vlingo.actors.Address;
import io.vlingo.actors.Stage;
import io.vlingo.developers.petclinic.model.Fullname;
import io.vlingo.common.Completes;

public interface Veterinarian {

  Completes<VeterinarianState> register(final Fullname name, final ContactInformation contact, final Specialty specialty);

  static Completes<VeterinarianState> register(final Stage stage, final Fullname name, final ContactInformation contact, final Specialty specialty) {
    final io.vlingo.actors.Address _address = stage.addressFactory().uniquePrefixedWith("g-");
    final Veterinarian _veterinarian = stage.actorFor(Veterinarian.class, Definition.has(VeterinarianEntity.class, Definition.parameters(_address.idString())), _address);
    return _veterinarian.register(name, contact, specialty);
  }

  Completes<VeterinarianState> changeName(final Fullname name);

  Completes<VeterinarianState> changeContactInformation(final ContactInformation contact);

  Completes<VeterinarianState> specializesIn(final Specialty specialty);

}