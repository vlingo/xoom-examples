package io.vlingo.xoom.petclinic.model.client;

import io.vlingo.xoom.petclinic.model.*;

public final class ClientState {

  public final String id;
  public final FullName name;
  public final ContactInformation contactInformation;

  public static ClientState identifiedBy(final String id) {
    return new ClientState(id, null, null);
  }

  public ClientState (final String id, final FullName name, final ContactInformation contactInformation) {
    this.id = id;
    this.name = name;
    this.contactInformation = contactInformation;
  }

  public ClientState register(final FullName name, final ContactInformation contactInformation) {
    return new ClientState(this.id, name, contactInformation);
  }

  public ClientState changeContactInformation(final ContactInformation contactInformation) {
    return new ClientState(this.id, this.name, contactInformation);
  }

  public ClientState changeName(final FullName name) {
    return new ClientState(this.id, name, this.contactInformation);
  }

}
