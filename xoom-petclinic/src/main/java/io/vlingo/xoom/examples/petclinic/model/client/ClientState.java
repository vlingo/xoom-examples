package io.vlingo.xoom.examples.petclinic.model.client;

import io.vlingo.xoom.examples.petclinic.model.ContactInformation;
import io.vlingo.xoom.examples.petclinic.model.Fullname;

public final class ClientState {

  public final String id;
  public final Fullname name;
  public final ContactInformation contact;

  public static ClientState identifiedBy(final String id) {
    return new ClientState(id, null, null);
  }

  public ClientState (final String id, final Fullname name, final ContactInformation contact) {
    this.id = id;
    this.name = name;
    this.contact = contact;
  }

  public ClientState register(final Fullname name, final ContactInformation contact) {
    return new ClientState(this.id, name, contact);
  }

  public ClientState changeName(final Fullname name) {
    return new ClientState(this.id, name, this.contact);
  }

  public ClientState changeContactInformation(final ContactInformation contact) {
    return new ClientState(this.id, this.name, contact);
  }

}
