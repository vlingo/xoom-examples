package io.vlingo.xoom.petclinic.infrastructure.resource;

import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;
import io.vlingo.xoom.annotation.autodispatch.Handler.Three;
import io.vlingo.xoom.annotation.autodispatch.Handler.Two;
import io.vlingo.xoom.annotation.autodispatch.HandlerEntry;

import io.vlingo.xoom.petclinic.infrastructure.persistence.ClientQueries;
import io.vlingo.xoom.petclinic.model.*;
import io.vlingo.xoom.petclinic.model.client.ClientState;
import io.vlingo.xoom.petclinic.model.client.Client;
import io.vlingo.xoom.petclinic.infrastructure.ClientData;
import java.util.Collection;

public class ClientResourceHandlers {

  public static final int REGISTER = 0;
  public static final int CHANGE_CONTACT_INFORMATION = 1;
  public static final int CHANGE_NAME = 2;
  public static final int CLIENTS = 3;
  public static final int ADAPT_STATE = 4;

  public static final HandlerEntry<Three<Completes<ClientState>, Stage, ClientData>> REGISTER_HANDLER =
          HandlerEntry.of(REGISTER, ($stage, data) -> {
              final FullName name = FullName.from(data.name.first, data.name.last);
              final PostalAddress postalAddress = PostalAddress.from(data.contactInformation.postalAddress.streetAddress, data.contactInformation.postalAddress.city, data.contactInformation.postalAddress.stateProvince, data.contactInformation.postalAddress.postalCode);
              final Telephone telephone = Telephone.from(data.contactInformation.telephone.number);
              final ContactInformation contactInformation = ContactInformation.from(postalAddress, telephone);
              return Client.register($stage, name, contactInformation);
          });

  public static final HandlerEntry<Three<Completes<ClientState>, Client, ClientData>> CHANGE_CONTACT_INFORMATION_HANDLER =
          HandlerEntry.of(CHANGE_CONTACT_INFORMATION, (client, data) -> {
              final PostalAddress postalAddress = PostalAddress.from(data.contactInformation.postalAddress.streetAddress, data.contactInformation.postalAddress.city, data.contactInformation.postalAddress.stateProvince, data.contactInformation.postalAddress.postalCode);
              final Telephone telephone = Telephone.from(data.contactInformation.telephone.number);
              final ContactInformation contactInformation = ContactInformation.from(postalAddress, telephone);
              return client.changeContactInformation(contactInformation);
          });

  public static final HandlerEntry<Three<Completes<ClientState>, Client, ClientData>> CHANGE_NAME_HANDLER =
          HandlerEntry.of(CHANGE_NAME, (client, data) -> {
              final FullName name = FullName.from(data.name.first, data.name.last);
              return client.changeName(name);
          });

  public static final HandlerEntry<Two<ClientData, ClientState>> ADAPT_STATE_HANDLER =
          HandlerEntry.of(ADAPT_STATE, ClientData::from);

  public static final HandlerEntry<Two<Completes<Collection<ClientData>>, ClientQueries>> QUERY_ALL_HANDLER =
          HandlerEntry.of(CLIENTS, ClientQueries::clients);

}