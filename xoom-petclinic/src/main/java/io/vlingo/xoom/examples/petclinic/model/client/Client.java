package io.vlingo.xoom.examples.petclinic.model.client;

import io.vlingo.xoom.examples.petclinic.model.ContactInformation;
import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.Address;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.examples.petclinic.model.Fullname;
import io.vlingo.xoom.common.Completes;

public interface Client {

  Completes<ClientState> register(final Fullname name, final ContactInformation contact);

  static Completes<ClientState> register(final Stage stage, final Fullname name, final ContactInformation contact) {
    final io.vlingo.xoom.actors.Address _address = stage.addressFactory().uniquePrefixedWith("g-");
    final Client _client = stage.actorFor(Client.class, Definition.has(ClientEntity.class, Definition.parameters(_address.idString())), _address);
    return _client.register(name, contact);
  }

  Completes<ClientState> changeName(final Fullname name);

  Completes<ClientState> changeContactInformation(final ContactInformation contact);

}