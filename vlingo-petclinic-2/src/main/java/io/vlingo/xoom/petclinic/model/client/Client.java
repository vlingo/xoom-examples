package io.vlingo.xoom.petclinic.model.client;

import io.vlingo.actors.Definition;
import io.vlingo.xoom.petclinic.model.*;
import io.vlingo.actors.Address;
import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;

public interface Client {

  Completes<ClientState> register(final FullName name, final ContactInformation contactInformation);

  static Completes<ClientState> register(final Stage stage, final FullName name, final ContactInformation contactInformation) {
    final io.vlingo.actors.Address _address = stage.addressFactory().uniquePrefixedWith("g-");
    final Client _client = stage.actorFor(Client.class, Definition.has(ClientEntity.class, Definition.parameters(_address.idString())), _address);
    return _client.register(name, contactInformation);
  }

  Completes<ClientState> changeContactInformation(final ContactInformation contactInformation);

  Completes<ClientState> changeName(final FullName name);

}