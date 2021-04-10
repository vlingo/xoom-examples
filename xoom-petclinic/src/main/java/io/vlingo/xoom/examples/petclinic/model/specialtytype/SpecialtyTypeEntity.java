package io.vlingo.xoom.examples.petclinic.model.specialtytype;

import io.vlingo.xoom.common.Completes;

import io.vlingo.xoom.lattice.model.sourcing.EventSourced;

/**
 * See <a href="https://docs.vlingo.io/xoom-lattice/entity-cqrs#sourced">EventSourced</a>
 */
public final class SpecialtyTypeEntity extends EventSourced implements SpecialtyType {
  private SpecialtyTypeState state;

  public SpecialtyTypeEntity(final String id) {
    super(id);
    this.state = SpecialtyTypeState.identifiedBy(id);
  }

  static {
    EventSourced.registerConsumer(SpecialtyTypeEntity.class, SpecialtyTypeOffered.class, SpecialtyTypeEntity::applySpecialtyTypeOffered);
    EventSourced.registerConsumer(SpecialtyTypeEntity.class, SpecialtyTypeRenamed.class, SpecialtyTypeEntity::applySpecialtyTypeRenamed);
  }

  public Completes<SpecialtyTypeState> offer(final String name) {
    return apply(new SpecialtyTypeOffered(state.id, name), () -> state);
  }

  public Completes<SpecialtyTypeState> rename(final String name) {
    return apply(new SpecialtyTypeRenamed(state.id, name), () -> state);
  }

  private void applySpecialtyTypeOffered(final SpecialtyTypeOffered event) {
    this.state = state.offer(event.name);
  }

  private void applySpecialtyTypeRenamed(final SpecialtyTypeRenamed event) {
    this.state = state.rename(event.name);
  }

  /*
   * Restores my initial state by means of {@code state}.
   *
   * @param snapshot the {@code SpecialtyTypeState} holding my state
   * @param currentVersion the int value of my current version; may be helpful in determining if snapshot is needed
   */
  @Override
  protected <SpecialtyTypeState> void restoreSnapshot(final SpecialtyTypeState snapshot, final int currentVersion) {
    // OVERRIDE FOR SNAPSHOT SUPPORT
    // See: https://docs.vlingo.io/xoom-lattice/entity-cqrs#eventsourced
  }

  /*
   * Answer the valid {@code SpecialtyTypeState} instance if a snapshot should
   * be taken and persisted along with applied {@code Source<T>} instance(s).
   *
   * @return SpecialtyTypeState
   */
  @Override
  protected SpecialtyTypeState snapshot() {
    // OVERRIDE FOR SNAPSHOT SUPPORT
    // See: https://docs.vlingo.io/xoom-lattice/entity-cqrs#eventsourced
    return null;
  }
}
