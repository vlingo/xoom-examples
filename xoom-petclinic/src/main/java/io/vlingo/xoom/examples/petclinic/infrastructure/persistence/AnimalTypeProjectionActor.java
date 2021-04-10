package io.vlingo.xoom.examples.petclinic.infrastructure.persistence;

import io.vlingo.xoom.examples.petclinic.infrastructure.AnimalTypeData;
import io.vlingo.xoom.examples.petclinic.infrastructure.Events;
import io.vlingo.xoom.examples.petclinic.model.animaltype.AnimalTypeRenamed;
import io.vlingo.xoom.examples.petclinic.model.animaltype.AnimalTypeTreatmentOffered;
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
public class AnimalTypeProjectionActor extends StateStoreProjectionActor<AnimalTypeData> {

  public AnimalTypeProjectionActor(final StateStore stateStore) {
    super(stateStore);
  }

  @Override
  protected AnimalTypeData currentDataFor(final Projectable projectable) {
    return AnimalTypeData.empty();
  }

  @Override
  protected AnimalTypeData merge(AnimalTypeData previousData, int previousVersion, AnimalTypeData currentData, int currentVersion) {
    for (final Source<?> event : sources()) {
      switch (Events.valueOf(event.typeName())) {
        case AnimalTypeRenamed:
          return mergeRename(previousData, typed(event));
        case AnimalTypeTreatmentOffered:
          return merge(typed(event));
        default:
          logger().warn("Event of type " + event.typeName() + " was not matched.");
      }
    }

    return previousData;
  }

  private AnimalTypeData merge(AnimalTypeTreatmentOffered offered){
    return AnimalTypeData.from(offered.id, offered.name);
  }

  private AnimalTypeData mergeRename(AnimalTypeData previous, AnimalTypeRenamed renamed){
    return AnimalTypeData.from(previous.id, renamed.name);
  }
}
