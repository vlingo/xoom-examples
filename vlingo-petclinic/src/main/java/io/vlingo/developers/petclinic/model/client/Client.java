package io.vlingo.developers.petclinic.model.client;

import io.vlingo.developers.petclinic.model.ContactInformation;
import io.vlingo.actors.Definition;
import io.vlingo.actors.Address;
import io.vlingo.actors.Stage;
import io.vlingo.developers.petclinic.model.Fullname;
import io.vlingo.common.Completes;

public interface Client {

  Completes<ClientState> register(final Fullname name, final ContactInformation contact);

  static Completes<ClientState> register(final Stage stage, final Fullname name, final ContactInformation contact) {
    final io.vlingo.actors.Address _address = stage.addressFactory().uniquePrefixedWith("g-");
    final Client _client = stage.actorFor(Client.class, Definition.has(ClientEntity.class, Definition.parameters(_address.idString())), _address);
    return _client.register(name, contact);
  }

  Completes<ClientState> changeName(final Fullname name);

  Completes<ClientState> changeContactInformation(final ContactInformation contact);

}