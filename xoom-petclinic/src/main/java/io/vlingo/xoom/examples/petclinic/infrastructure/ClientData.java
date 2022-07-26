package io.vlingo.xoom.examples.petclinic.infrastructure;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vlingo.xoom.examples.petclinic.model.client.ClientState;

public class ClientData {
  public final String id;
  public final FullNameData name;
  public final ContactInformationData contactInformation;

  public static ClientData from(final ClientState clientState) {
    final FullNameData name = clientState.name != null ? FullNameData.from(clientState.name) : null;
    final ContactInformationData contactInformation = clientState.contactInformation != null ? ContactInformationData.from(clientState.contactInformation) : null;
    return from(clientState.id, name, contactInformation);
  }

  @JsonCreator
  public static ClientData from(@JsonProperty("id") final String id,
                                @JsonProperty("name") final FullNameData name,
                                @JsonProperty("contactInformation") final ContactInformationData contactInformation) {
    return new ClientData(id, name, contactInformation);
  }

  public static List<ClientData> from(final List<ClientState> states) {
    return states.stream().map(ClientData::from).collect(Collectors.toList());
  }

  public static ClientData empty() {
    return from(ClientState.identifiedBy(""));
  }

  private ClientData (final String id, final FullNameData name, final ContactInformationData contactInformation) {
    this.id = id;
    this.name = name;
    this.contactInformation = contactInformation;
  }

}
