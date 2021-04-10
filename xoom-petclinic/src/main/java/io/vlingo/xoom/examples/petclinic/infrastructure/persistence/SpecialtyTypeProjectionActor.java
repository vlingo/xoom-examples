package io.vlingo.xoom.examples.petclinic.infrastructure.persistence;

import io.vlingo.xoom.examples.petclinic.infrastructure.Events;
import io.vlingo.xoom.examples.petclinic.infrastructure.SpecialtyTypeData;
import io.vlingo.xoom.examples.petclinic.model.specialtytype.SpecialtyTypeOffered;
import io.vlingo.xoom.examples.petclinic.model.specialtytype.SpecialtyTypeRenamed;

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
public class SpecialtyTypeProjectionActor extends StateStoreProjectionActor<SpecialtyTypeData> {

  public SpecialtyTypeProjectionActor(final StateStore stateStore) {
    super(stateStore);
  }

  @Override
  protected SpecialtyTypeData currentDataFor(final Projectable projectable) {
    return SpecialtyTypeData.empty();
  }

  @Override
  protected SpecialtyTypeData merge(SpecialtyTypeData previousData, int previousVersion, SpecialtyTypeData currentData, int currentVersion) {
    for (final Source<?> event : sources()) {
      switch (Events.valueOf(event.typeName())) {
        case SpecialtyTypeOffered:
          return merge(typed(event));
        case SpecialtyTypeRenamed:
          return mergeRename(previousData, typed(event));
        default:
          logger().warn("Event of type " + event.typeName() + " was not matched.");
          break;
      }
    }

    return previousData;
  }

  private SpecialtyTypeData merge(SpecialtyTypeOffered offered){
    return SpecialtyTypeData.from(offered.id, offered.name);
  }

  private SpecialtyTypeData mergeRename(SpecialtyTypeData previous, SpecialtyTypeRenamed renamed){
    return SpecialtyTypeData.from(previous.id, renamed.name);
  }
}
