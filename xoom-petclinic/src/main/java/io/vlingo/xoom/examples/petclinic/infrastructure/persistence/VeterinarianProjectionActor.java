package io.vlingo.xoom.examples.petclinic.infrastructure.persistence;

import io.vlingo.xoom.examples.petclinic.infrastructure.Events;
import io.vlingo.xoom.examples.petclinic.infrastructure.*;
import io.vlingo.xoom.examples.petclinic.model.veterinarian.*;

import io.vlingo.xoom.lattice.model.projection.Projectable;
import io.vlingo.xoom.lattice.model.projection.StateStoreProjectionActor;
import io.vlingo.xoom.symbio.Source;
import io.vlingo.xoom.symbio.store.state.StateStore;
import io.vlingo.xoom.turbo.ComponentRegistry;

/**
 * See
 * <a href="https://docs.vlingo.io/xoom-lattice/projections#implementing-with-the-statestoreprojectionactor">
 *   StateStoreProjectionActor
 * </a>
 */
public class VeterinarianProjectionActor extends StateStoreProjectionActor<VeterinarianData> {

  private static final VeterinarianData Empty = VeterinarianData.empty();

  public VeterinarianProjectionActor() {
    this(ComponentRegistry.withType(QueryModelStateStoreProvider.class).store);
  }

  public VeterinarianProjectionActor(final StateStore stateStore) {
    super(stateStore);
  }

  @Override
  protected VeterinarianData currentDataFor(final Projectable projectable) {
    return Empty;
  }

  @Override
  protected VeterinarianData merge(final VeterinarianData previousData, final int previousVersion, final VeterinarianData currentData, final int currentVersion) {

    if (previousVersion == currentVersion) return currentData;

    VeterinarianData merged = previousData;

    for (final Source<?> event : sources()) {
      switch (Events.valueOf(event.typeName())) {
        case VeterinarianRegistered: {
          final VeterinarianRegistered typedEvent = typed(event);
          final FullNameData name = FullNameData.from(typedEvent.name.first, typedEvent.name.last);
          final PostalAddressData postalAddress = PostalAddressData.from(typedEvent.contactInformation.postalAddress.streetAddress, typedEvent.contactInformation.postalAddress.city, typedEvent.contactInformation.postalAddress.stateProvince, typedEvent.contactInformation.postalAddress.postalCode);
          final TelephoneData telephone = TelephoneData.from(typedEvent.contactInformation.telephone.number);
          final ContactInformationData contactInformation = ContactInformationData.from(postalAddress, telephone);
          final SpecialtyData specialty = SpecialtyData.from(typedEvent.specialty.specialtyTypeId);
          merged = VeterinarianData.from(typedEvent.id, name, contactInformation, specialty);
          break;
        }

        case VeterinarianContactInformationChanged: {
          final VeterinarianContactInformationChanged typedEvent = typed(event);
          final PostalAddressData postalAddress = PostalAddressData.from(typedEvent.contactInformation.postalAddress.streetAddress, typedEvent.contactInformation.postalAddress.city, typedEvent.contactInformation.postalAddress.stateProvince, typedEvent.contactInformation.postalAddress.postalCode);
          final TelephoneData telephone = TelephoneData.from(typedEvent.contactInformation.telephone.number);
          final ContactInformationData contactInformation = ContactInformationData.from(postalAddress, telephone);
          merged = VeterinarianData.from(typedEvent.id, previousData.name, contactInformation, previousData.specialty);
          break;
        }

        case VeterinarianNameChanged: {
          final VeterinarianNameChanged typedEvent = typed(event);
          final FullNameData name = FullNameData.from(typedEvent.name.first, typedEvent.name.last);
          merged = VeterinarianData.from(typedEvent.id, name, previousData.contactInformation, previousData.specialty);
          break;
        }

        case VeterinarianSpecialtyChosen: {
          final VeterinarianSpecialtyChosen typedEvent = typed(event);
          final SpecialtyData specialty = SpecialtyData.from(typedEvent.specialty.specialtyTypeId);
          merged = VeterinarianData.from(typedEvent.id, previousData.name, previousData.contactInformation, specialty);
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
