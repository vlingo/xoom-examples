package io.vlingo.xoom.examples.petclinic.model.pet;

import io.vlingo.xoom.examples.petclinic.model.client.Kind;
import io.vlingo.xoom.examples.petclinic.model.client.Name;
import io.vlingo.xoom.examples.petclinic.model.client.Owner;
import io.vlingo.xoom.common.Completes;

import io.vlingo.xoom.lattice.model.sourcing.EventSourced;

/**
 * See <a href="https://docs.vlingo.io/xoom-lattice/entity-cqrs#sourced">EventSourced</a>
 */
public final class PetEntity extends EventSourced implements Pet {
  private PetState state;

  public PetEntity(final String id) {
    super(id);
    this.state = PetState.identifiedBy(id);
  }

  static {
    EventSourced.registerConsumer(PetEntity.class, PetRegistered.class, PetEntity::applyPetRegistered);
    EventSourced.registerConsumer(PetEntity.class, PetNameChanged.class, PetEntity::applyPetNameChanged);
    EventSourced.registerConsumer(PetEntity.class, PetBirthRecorded.class, PetEntity::applyPetBirthRecorded);
    EventSourced.registerConsumer(PetEntity.class, PetDeathRecorded.class, PetEntity::applyPetDeathRecorded);
    EventSourced.registerConsumer(PetEntity.class, PetKindCorrected.class, PetEntity::applyPetKindCorrected);
    EventSourced.registerConsumer(PetEntity.class, PetOwnerChanged.class, PetEntity::applyPetOwnerChanged);
  }

  public Completes<PetState> register(final Name name, final long birth, final Kind kind, final Owner owner) {
    return apply(new PetRegistered(state.id, name, birth, kind, owner), () -> state);
  }

  public Completes<PetState> changeName(final Name name) {
    return apply(new PetNameChanged(state.id, name), () -> state);
  }

  public Completes<PetState> recordBirth(final long birth) {
    return apply(new PetBirthRecorded(state.id, birth), () -> state);
  }

  public Completes<PetState> recordDeath(final long death) {
    return apply(new PetDeathRecorded(state.id, death), () -> state);
  }

  public Completes<PetState> correctKind(final Kind kind) {
    return apply(new PetKindCorrected(state.id, kind), () -> state);
  }

  public Completes<PetState> changeOwner(final Owner owner) {
    return apply(new PetOwnerChanged(state.id, owner), () -> state);
  }

  private void applyPetRegistered(final PetRegistered event) {
    this.state = state.register(event.name, event.birth, event.kind, event.owner);
  }

  private void applyPetNameChanged(final PetNameChanged event) {
    this.state = state.changeName(event.name);
  }

  private void applyPetBirthRecorded(final PetBirthRecorded event) {
    this.state = state.recordBirth(event.birth);
  }

  private void applyPetDeathRecorded(final PetDeathRecorded event) {
    this.state = state.recordDeath(event.death);
  }

  private void applyPetKindCorrected(final PetKindCorrected event) {
    this.state = state.correctKind(event.kind);
  }

  private void applyPetOwnerChanged(final PetOwnerChanged event) {
    this.state = state.changeOwner(event.owner);
  }

  /*
   * Restores my initial state by means of {@code state}.
   *
   * @param snapshot the {@code PetState} holding my state
   * @param currentVersion the int value of my current version; may be helpful in determining if snapshot is needed
   */
  @Override
  protected <PetState> void restoreSnapshot(final PetState snapshot, final int currentVersion) {
    // OVERRIDE FOR SNAPSHOT SUPPORT
    // See: https://docs.vlingo.io/xoom-lattice/entity-cqrs#eventsourced
  }

  /*
   * Answer the valid {@code PetState} instance if a snapshot should
   * be taken and persisted along with applied {@code Source<T>} instance(s).
   *
   * @return PetState
   */
  @Override
  protected PetState snapshot() {
    // OVERRIDE FOR SNAPSHOT SUPPORT
    // See: https://docs.vlingo.io/xoom-lattice/entity-cqrs#eventsourced
    return null;
  }
}
