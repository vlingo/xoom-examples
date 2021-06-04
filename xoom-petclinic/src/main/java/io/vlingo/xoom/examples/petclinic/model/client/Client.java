package io.vlingo.xoom.examples.petclinic.model.client;

import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.examples.petclinic.model.*;
import io.vlingo.xoom.actors.Address;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.common.Completes;

public interface Client {

  Completes<ClientState> register(final FullName name, final ContactInformation contactInformation);

  static Completes<ClientState> register(final Stage stage, final FullName name, final ContactInformation contactInformation) {
    final io.vlingo.xoom.actors.Address _address = stage.addressFactory().uniquePrefixedWith("g-");
    final Client _client = stage.actorFor(Client.class, Definition.has(ClientEntity.class, Definition.parameters(_address.idString())), _address);
    return _client.register(name, contactInformation);
  }

  Completes<ClientState> changeContactInformation(final ContactInformation contactInformation);

  Completes<ClientState> changeName(final FullName name);

}