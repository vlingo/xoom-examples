package io.vlingo.xoom.petclinic.infrastructure.persistence;

import io.vlingo.xoom.petclinic.model.animaltype.*;
import io.vlingo.xoom.petclinic.infrastructure.Events;
import io.vlingo.xoom.petclinic.infrastructure.*;

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
public class AnimalTypeProjectionActor extends StateStoreProjectionActor<AnimalTypeData> {

  private static final AnimalTypeData Empty = AnimalTypeData.empty();

  public AnimalTypeProjectionActor() {
    this(QueryModelStateStoreProvider.instance().store);
  }

  public AnimalTypeProjectionActor(final StateStore stateStore) {
    super(stateStore);
  }

  @Override
  protected AnimalTypeData currentDataFor(final Projectable projectable) {
    return Empty;
  }

  @Override
  protected AnimalTypeData merge(final AnimalTypeData previousData, final int previousVersion, final AnimalTypeData currentData, final int currentVersion) {

    if (previousVersion == currentVersion) return currentData;

    AnimalTypeData merged = previousData;

    for (final Source<?> event : sources()) {
      switch (Events.valueOf(event.typeName())) {
        case AnimalTypeTreatmentOffered: {
          final AnimalTypeTreatmentOffered typedEvent = typed(event);
          merged = AnimalTypeData.from(typedEvent.id, typedEvent.name);
          break;
        }

        case AnimalTypeRenamed: {
          final AnimalTypeRenamed typedEvent = typed(event);
          merged = AnimalTypeData.from(typedEvent.id, typedEvent.name);
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
