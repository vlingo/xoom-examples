package io.vlingo.xoom.examples.petclinic.infrastructure;

import java.util.List;
import java.util.stream.Collectors;
import io.vlingo.xoom.examples.petclinic.model.client.ClientState;

public class ClientData {
  public final String id;
  public final FullnameData name;
  public final ContactInformationData contact;

  public static ClientData from(final ClientState state) {
    return new ClientData(state);
  }

  public static List<ClientData> from(final List<ClientState> states) {
    return states.stream().map(ClientData::from).collect(Collectors.toList());
  }

  public static ClientData from(String id, FullnameData name, ContactInformationData contact){
    return new ClientData(id, name, contact);
  }

  public static ClientData empty() {
    return ClientData.from("", null, null);
  }

  private ClientData (final ClientState state) {
    this.id = state.id;
    this.name = FullnameData.of(state.name.first, state.name.last);
    this.contact = ContactInformationData.of(state.contact.postalAddress, state.contact.telephone);
  }

  private ClientData(String id, FullnameData name, ContactInformationData contact) {
    this.id = id;
    this.name = name;
    this.contact = contact;
  }
}
