package io.vlingo.xoom.examples.petclinic.infrastructure.persistence;

import io.vlingo.xoom.examples.petclinic.infrastructure.ClientData;
import io.vlingo.xoom.examples.petclinic.infrastructure.ContactInformationData;
import io.vlingo.xoom.examples.petclinic.infrastructure.Events;
import io.vlingo.xoom.examples.petclinic.infrastructure.FullnameData;
import io.vlingo.xoom.examples.petclinic.model.client.ClientContactInformationChanged;
import io.vlingo.xoom.examples.petclinic.model.client.ClientNameChanged;
import io.vlingo.xoom.examples.petclinic.model.client.ClientRegistered;
import io.vlingo.xoom.lattice.model.projection.Projectable;
import io.vlingo.xoom.lattice.model.projection.StateStoreProjectionActor;
import io.vlingo.xoom.symbio.Source;
import io.vlingo.xoom.symbio.store.state.StateStore;

/**
 * See
 * <a href="https://docs.vlingo.io/xoom-lattice/projections#implementing-with-the-statestoreprojectionactor">
 *   StateStoreProjectionActor
 * </a>
 */
public class ClientProjectionActor extends StateStoreProjectionActor<ClientData> {

  public ClientProjectionActor(StateStore stateStore) {
    super(stateStore);
  }

  @Override
  protected ClientData currentDataFor(final Projectable projectable) {
    return ClientData.empty();
  }

  @Override
  protected ClientData merge(ClientData previousData, int previousVersion, ClientData currentData, int currentVersion) {
    for (final Source<?> event : sources()) {
      switch (Events.valueOf(event.typeName())) {
        case ClientRegistered:
          return merge(typed(event));
        case ClientNameChanged:
          return mergeNameChange(previousData, typed(event));
        case ClientContactInformationChanged:
          return mergeContactChange(previousData, typed(event));
        default:
          logger().warn("Event of type " + event.typeName() + " was not matched.");
          break;
      }
    }

    return previousData;
  }

  private ClientData merge(ClientRegistered registered){
    final FullnameData name = FullnameData.of(registered.name.first, registered.name.last);
    final ContactInformationData contact = ContactInformationData.of(registered.contact.postalAddress, registered.contact.telephone);
    return ClientData.from(registered.id, name, contact);
  }

  private ClientData mergeNameChange(ClientData previous, ClientNameChanged nameChanged){
    final FullnameData name = FullnameData.of(nameChanged.name.first, nameChanged.name.last);
    return ClientData.from(previous.id, name, previous.contact);
  }

  private ClientData mergeContactChange(ClientData previous, ClientContactInformationChanged contactChanged){
    final ContactInformationData contact
            = ContactInformationData.of(contactChanged.contact.postalAddress, contactChanged.contact.telephone);
    return ClientData.from(previous.id, previous.name, contact);
  }
}
