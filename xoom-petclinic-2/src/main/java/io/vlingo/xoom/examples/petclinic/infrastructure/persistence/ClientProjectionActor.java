package io.vlingo.xoom.examples.petclinic.infrastructure.persistence;

import io.vlingo.xoom.examples.petclinic.infrastructure.Events;
import io.vlingo.xoom.examples.petclinic.infrastructure.*;
import io.vlingo.xoom.examples.petclinic.model.client.*;

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

  private static final ClientData Empty = ClientData.empty();

  public ClientProjectionActor() {
    this(QueryModelStateStoreProvider.instance().store);
  }

  public ClientProjectionActor(final StateStore stateStore) {
    super(stateStore);
  }

  @Override
  protected ClientData currentDataFor(final Projectable projectable) {
    return Empty;
  }

  @Override
  protected ClientData merge(final ClientData previousData, final int previousVersion, final ClientData currentData, final int currentVersion) {

    if (previousVersion == currentVersion) return currentData;

    ClientData merged = previousData;

    for (final Source<?> event : sources()) {
      switch (Events.valueOf(event.typeName())) {
        case ClientRegistered: {
          final ClientRegistered typedEvent = typed(event);
          final FullNameData name = FullNameData.from(typedEvent.name.first, typedEvent.name.last);
          final PostalAddressData postalAddress = PostalAddressData.from(typedEvent.contactInformation.postalAddress.streetAddress, typedEvent.contactInformation.postalAddress.city, typedEvent.contactInformation.postalAddress.stateProvince, typedEvent.contactInformation.postalAddress.postalCode);
          final TelephoneData telephone = TelephoneData.from(typedEvent.contactInformation.telephone.number);
          final ContactInformationData contactInformation = ContactInformationData.from(postalAddress, telephone);
          merged = ClientData.from(typedEvent.id, name, contactInformation);
          break;
        }

        case ClientContactInformationChanged: {
          final ClientContactInformationChanged typedEvent = typed(event);
          final PostalAddressData postalAddress = PostalAddressData.from(typedEvent.contactInformation.postalAddress.streetAddress, typedEvent.contactInformation.postalAddress.city, typedEvent.contactInformation.postalAddress.stateProvince, typedEvent.contactInformation.postalAddress.postalCode);
          final TelephoneData telephone = TelephoneData.from(typedEvent.contactInformation.telephone.number);
          final ContactInformationData contactInformation = ContactInformationData.from(postalAddress, telephone);
          merged = ClientData.from(typedEvent.id, previousData.name, contactInformation);
          break;
        }

        case ClientNameChanged: {
          final ClientNameChanged typedEvent = typed(event);
          final FullNameData name = FullNameData.from(typedEvent.name.first, typedEvent.name.last);
          merged = ClientData.from(typedEvent.id, name, previousData.contactInformation);
          break;
        }

        default:
          logger().warn("Event of type " + event.typeName() + " was not matched.");
          break;
      }
    }

    return merged;
  }
}
