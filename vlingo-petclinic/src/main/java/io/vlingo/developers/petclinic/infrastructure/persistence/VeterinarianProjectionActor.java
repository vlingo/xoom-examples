package io.vlingo.developers.petclinic.infrastructure.persistence;

import io.vlingo.developers.petclinic.infrastructure.*;
import io.vlingo.developers.petclinic.model.veterinarian.*;

import io.vlingo.lattice.model.projection.Projectable;
import io.vlingo.lattice.model.projection.StateStoreProjectionActor;
import io.vlingo.symbio.Source;

/**
 * See
 * <a href="https://docs.vlingo.io/vlingo-lattice/projections#implementing-with-the-statestoreprojectionactor">
 *   StateStoreProjectionActor
 * </a>
 */
public class VeterinarianProjectionActor extends StateStoreProjectionActor<VeterinarianData> {

  public VeterinarianProjectionActor() {
    super(QueryModelStateStoreProvider.instance().store);
  }

  @Override
  protected VeterinarianData currentDataFor(final Projectable projectable) {
    return VeterinarianData.empty();
  }

  @Override
  protected VeterinarianData merge(VeterinarianData previousData, int previousVersion, VeterinarianData currentData, int currentVersion) {
    for (final Source<?> event : sources()) {
      switch (Events.valueOf(event.typeName())) {
        case VeterinarianContactInformationChanged:
          return mergeContactChange(previousData, typed(event));
        case VeterinarianSpecialtyChosen:
          return mergeSpecializeIn(previousData, typed(event));
        case VeterinarianRegistered:
          return merge(typed(event));
        case VeterinarianNameChanged:
          return mergeNameChange(previousData, typed(event));
        default:
          logger().warn("Event of type " + event.typeName() + " was not matched.");
          break;
      }
    }

    return previousData;
  }

  private VeterinarianData merge(final VeterinarianRegistered registered){
    final FullnameData fullname = FullnameData.of(registered.name.first, registered.name.last);
    final ContactInformationData contactInformation = ContactInformationData.of(
            registered.contact.postalAddress, registered.contact.telephone
    );
    final SpecialtyData specialty = SpecialtyData.of(registered.specialty.specialtyTypeId);
    return VeterinarianData.from(registered.id, fullname, contactInformation, specialty);
  }

  private VeterinarianData mergeContactChange(final VeterinarianData previous,
                                 final VeterinarianContactInformationChanged contactChanged){
    final ContactInformationData contact
            = ContactInformationData.of(contactChanged.contact.postalAddress, contactChanged.contact.telephone);
    return VeterinarianData.from(previous.id, previous.name, contact, previous.specialty);
  }

  private VeterinarianData mergeNameChange(final VeterinarianData previous,
                                 final VeterinarianNameChanged nameChanged){
    final FullnameData name = FullnameData.of(nameChanged.name.first, nameChanged.name.last);
    return VeterinarianData.from(previous.id, name, previous.contact, previous.specialty);
  }

  private VeterinarianData mergeSpecializeIn(final VeterinarianData previous,
                                             final VeterinarianSpecialtyChosen specialtyChosen){
    final SpecialtyData specialty = SpecialtyData.of(specialtyChosen.specialty.specialtyTypeId);
    return VeterinarianData.from(previous.id, previous.name, previous.contact, specialty);
  }
}
