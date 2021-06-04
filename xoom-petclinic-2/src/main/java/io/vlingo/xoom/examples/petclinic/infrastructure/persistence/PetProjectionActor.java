package io.vlingo.xoom.examples.petclinic.infrastructure.persistence;

import io.vlingo.xoom.examples.petclinic.infrastructure.Events;
import io.vlingo.xoom.examples.petclinic.infrastructure.*;
import io.vlingo.xoom.examples.petclinic.model.pet.*;

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
public class PetProjectionActor extends StateStoreProjectionActor<PetData> {

  private static final PetData Empty = PetData.empty();

  public PetProjectionActor() {
    this(ComponentRegistry.withType(QueryModelStateStoreProvider.class).store);
  }

  public PetProjectionActor(final StateStore stateStore) {
    super(stateStore);
  }

  @Override
  protected PetData currentDataFor(final Projectable projectable) {
    return Empty;
  }

  @Override
  protected PetData merge(final PetData previousData, final int previousVersion, final PetData currentData, final int currentVersion) {

    if (previousVersion == currentVersion) return currentData;

    PetData merged = previousData;

    for (final Source<?> event : sources()) {
      switch (Events.valueOf(event.typeName())) {
        case PetRegistered: {
          final PetRegistered typedEvent = typed(event);
          final NameData name = NameData.from(typedEvent.name.value);
          final DateData birth = DateData.from(typedEvent.birth.value);
          final DateData death = DateData.from(typedEvent.death.value);
          final KindData kind = KindData.from(typedEvent.kind.animalTypeId);
          final OwnerData owner = OwnerData.from(typedEvent.owner.clientId);
          merged = PetData.from(typedEvent.id, name, birth, death, kind, owner);
          break;
        }

        case PetNameChanged: {
          final PetNameChanged typedEvent = typed(event);
          final NameData name = NameData.from(typedEvent.name.value);
          merged = PetData.from(typedEvent.id, name, previousData.birth, previousData.death, previousData.kind, previousData.owner);
          break;
        }

        case PetBirthRecorded: {
          final PetBirthRecorded typedEvent = typed(event);
          final DateData birth = DateData.from(typedEvent.birth.value);
          merged = PetData.from(typedEvent.id, previousData.name, birth, previousData.death, previousData.kind, previousData.owner);
          break;
        }

        case PetDeathRecorded: {
          final PetDeathRecorded typedEvent = typed(event);
          final DateData death = DateData.from(typedEvent.death.value);
          merged = PetData.from(typedEvent.id, previousData.name, previousData.birth, death, previousData.kind, previousData.owner);
          break;
        }

        case PetKindCorrected: {
          final PetKindCorrected typedEvent = typed(event);
          final KindData kind = KindData.from(typedEvent.kind.animalTypeId);
          merged = PetData.from(typedEvent.id, previousData.name, previousData.birth, previousData.death, kind, previousData.owner);
          break;
        }

        case PetOwnerChanged: {
          final PetOwnerChanged typedEvent = typed(event);
          final OwnerData owner = OwnerData.from(typedEvent.owner.clientId);
          merged = PetData.from(typedEvent.id, previousData.name, previousData.birth, previousData.death, previousData.kind, owner);
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
