package io.vlingo.xoom.petclinic.infrastructure.persistence;

import io.vlingo.xoom.petclinic.infrastructure.Events;
import io.vlingo.xoom.petclinic.infrastructure.*;
import io.vlingo.xoom.petclinic.model.specialtytype.*;

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
public class SpecialtyTypeProjectionActor extends StateStoreProjectionActor<SpecialtyTypeData> {

  private static final SpecialtyTypeData Empty = SpecialtyTypeData.empty();

  public SpecialtyTypeProjectionActor() {
    this(QueryModelStateStoreProvider.instance().store);
  }

  public SpecialtyTypeProjectionActor(final StateStore stateStore) {
    super(stateStore);
  }

  @Override
  protected SpecialtyTypeData currentDataFor(final Projectable projectable) {
    return Empty;
  }

  @Override
  protected SpecialtyTypeData merge(final SpecialtyTypeData previousData, final int previousVersion, final SpecialtyTypeData currentData, final int currentVersion) {

    if (previousVersion == currentVersion) return currentData;

    SpecialtyTypeData merged = previousData;

    for (final Source<?> event : sources()) {
      switch (Events.valueOf(event.typeName())) {
        case SpecialtyTypeOffered: {
          final SpecialtyTypeOffered typedEvent = typed(event);
          merged = SpecialtyTypeData.from(typedEvent.id, typedEvent.name);
          break;
        }

        case SpecialtyTypeRenamed: {
          final SpecialtyTypeRenamed typedEvent = typed(event);
          merged = SpecialtyTypeData.from(typedEvent.id, typedEvent.name);
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
