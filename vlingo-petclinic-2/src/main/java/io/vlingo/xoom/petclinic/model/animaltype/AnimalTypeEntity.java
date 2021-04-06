package io.vlingo.xoom.petclinic.model.animaltype;

import io.vlingo.common.Completes;

import io.vlingo.lattice.model.sourcing.EventSourced;

/**
 * See <a href="https://docs.vlingo.io/vlingo-lattice/entity-cqrs#sourced">EventSourced</a>
 */
public final class AnimalTypeEntity extends EventSourced implements AnimalType {
  private AnimalTypeState state;

  public AnimalTypeEntity(final String id) {
    super(id);
    this.state = AnimalTypeState.identifiedBy(id);
  }

  static {
    EventSourced.registerConsumer(AnimalTypeEntity.class, AnimalTypeTreatmentOffered.class, AnimalTypeEntity::applyAnimalTypeTreatmentOffered);
    EventSourced.registerConsumer(AnimalTypeEntity.class, AnimalTypeRenamed.class, AnimalTypeEntity::applyAnimalTypeRenamed);
  }

  @Override
  public Completes<AnimalTypeState> offerTreatmentFor(final String name) {
    final AnimalTypeState stateArg = state.offerTreatmentFor(name);
    return apply(new AnimalTypeTreatmentOffered(stateArg), () -> state);
  }

  @Override
  public Completes<AnimalTypeState> rename(final String name) {
    final AnimalTypeState stateArg = state.rename(name);
    return apply(new AnimalTypeRenamed(stateArg), () -> state);
  }

  private void applyAnimalTypeTreatmentOffered(final AnimalTypeTreatmentOffered event) {
    state = state.offerTreatmentFor(event.name);
  }

  private void applyAnimalTypeRenamed(final AnimalTypeRenamed event) {
    state = state.rename(event.name);
  }

  /*
   * Restores my initial state by means of {@code state}.
   *
   * @param snapshot the {@code AnimalTypeState} holding my state
   * @param currentVersion the int value of my current version; may be helpful in determining if snapshot is needed
   */
  @Override
  protected <AnimalTypeState> void restoreSnapshot(final AnimalTypeState snapshot, final int currentVersion) {
    // OVERRIDE FOR SNAPSHOT SUPPORT
    // See: https://docs.vlingo.io/vlingo-lattice/entity-cqrs#eventsourced
  }

  /*
   * Answer the valid {@code AnimalTypeState} instance if a snapshot should
   * be taken and persisted along with applied {@code Source<T>} instance(s).
   *
   * @return AnimalTypeState
   */
  @Override
  protected AnimalTypeState snapshot() {
    // OVERRIDE FOR SNAPSHOT SUPPORT
    // See: https://docs.vlingo.io/vlingo-lattice/entity-cqrs#eventsourced
    return null;
  }
}
