package io.vlingo.developers.petclinic.infrastructure.persistence;

import io.vlingo.developers.petclinic.infrastructure.*;
import io.vlingo.developers.petclinic.model.pet.*;

import io.vlingo.lattice.model.projection.Projectable;
import io.vlingo.lattice.model.projection.StateStoreProjectionActor;
import io.vlingo.symbio.Source;
import io.vlingo.symbio.store.state.StateStore;

/**
 * See
 * <a href="https://docs.vlingo.io/vlingo-lattice/projections#implementing-with-the-statestoreprojectionactor">
 *   StateStoreProjectionActor
 * </a>
 */
public class PetProjectionActor extends StateStoreProjectionActor<PetData> {

  public PetProjectionActor() {
    super(QueryModelStateStoreProvider.instance().store);
  }

  public PetProjectionActor(final StateStore store) {
    super(store);
  }

  @Override
  protected PetData currentDataFor(final Projectable projectable) {
    return PetData.empty();
  }

  @Override
  protected PetData merge(PetData previousData, int previousVersion, PetData currentData, int currentVersion) {
    for (final Source<?> event : sources()) {
      switch (Events.valueOf(event.typeName())) {
        case PetKindCorrected:
          return mergeKindCorrect(previousData, typed(event));
        case PetNameChanged:
          return mergeNameChange(previousData, typed(event));
        case PetRegistered:
          return merge(typed(event));
        case PetDeathRecorded:
          return mergeDeathRecord(previousData, typed(event));
        case PetOwnerChanged:
          return mergeOwnerChange(previousData, typed(event));
        case PetBirthRecorded:
          return mergeBirthRecorded(previousData, typed(event));
        default:
          logger().warn("Event of type " + event.typeName() + " was not matched.");
          break;
      }
    }

    return previousData;
  }

  private PetData merge(PetRegistered registered){
    final NameData name = NameData.of(registered.name.value);
    final KindData kind = KindData.of(registered.kind.animalTypeId);
    final OwnerData owner = OwnerData.of(registered.owner.clientId);
    return PetData.from(registered.id, name, registered.birth, 0L, kind, owner, null);
  }

  private PetData mergeKindCorrect(PetData previous, PetKindCorrected kindCorrected){
    final KindData kind = KindData.of(kindCorrected.kind.animalTypeId);
    return PetData.from(previous.id, previous.name, previous.birth, previous.death, kind, previous.owner, previous.visit);
  }

  private PetData mergeNameChange(PetData previous, PetNameChanged nameChanged){
    final NameData name = NameData.of(nameChanged.name.value);
    return PetData.from(previous.id, name, previous.birth, previous.death, previous.kind, previous.owner, previous.visit);
  }

  private PetData mergeBirthRecorded(PetData previous, PetBirthRecorded birthRecorded){
    return PetData.from(previous.id, previous.name, birthRecorded.birth, previous.death, previous.kind, previous.owner, previous.visit);
  }

  private PetData mergeDeathRecord(PetData previous, PetDeathRecorded deathRecorded){
    return PetData.from(previous.id, previous.name, previous.birth, deathRecorded.death, previous.kind, previous.owner, previous.visit);
  }

  private PetData mergeOwnerChange(PetData previous, PetOwnerChanged ownerChanged){
    final OwnerData owner = OwnerData.of(ownerChanged.owner.clientId);
    return PetData.from(previous.id, previous.name, previous.birth, previous.death, previous.kind, owner, previous.visit);
  }
}
