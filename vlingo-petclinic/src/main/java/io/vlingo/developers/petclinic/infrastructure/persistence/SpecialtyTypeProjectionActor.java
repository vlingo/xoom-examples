package io.vlingo.developers.petclinic.infrastructure.persistence;

import io.vlingo.developers.petclinic.infrastructure.Events;
import io.vlingo.developers.petclinic.infrastructure.SpecialtyTypeData;
import io.vlingo.developers.petclinic.model.specialtytype.SpecialtyTypeOffered;
import io.vlingo.developers.petclinic.model.specialtytype.SpecialtyTypeRenamed;

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

  public SpecialtyTypeProjectionActor() {
    super(QueryModelStateStoreProvider.instance().store);
  }

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
