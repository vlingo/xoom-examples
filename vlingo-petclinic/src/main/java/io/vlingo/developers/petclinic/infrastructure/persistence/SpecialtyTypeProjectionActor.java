package io.vlingo.developers.petclinic.infrastructure.persistence;

import io.vlingo.developers.petclinic.infrastructure.Events;
import io.vlingo.developers.petclinic.infrastructure.SpecialtyTypeData;
import io.vlingo.developers.petclinic.model.specialtytype.SpecialtyTypeState;

import io.vlingo.lattice.model.projection.Projectable;
import io.vlingo.lattice.model.projection.StateStoreProjectionActor;
import io.vlingo.symbio.Source;

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

  @Override
  protected SpecialtyTypeData currentDataFor(final Projectable projectable) {
    return SpecialtyTypeData.empty();
  }

  @Override
  protected SpecialtyTypeData merge(SpecialtyTypeData previousData, int previousVersion, SpecialtyTypeData currentData, int currentVersion) {
    for (final Source<?> event : sources()) {
      switch (Events.valueOf(event.typeName())) {
        case SpecialtyTypeOffered:
          return SpecialtyTypeData.empty();   // TODO: implement actual merge
        case SpecialtyTypeRenamed:
          return SpecialtyTypeData.empty();   // TODO: implement actual merge
        default:
          logger().warn("Event of type " + event.typeName() + " was not matched.");
          break;
      }
    }

    return previousData;
  }
}
