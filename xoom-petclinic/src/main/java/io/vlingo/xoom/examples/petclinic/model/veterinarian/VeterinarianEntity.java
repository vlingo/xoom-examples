package io.vlingo.xoom.examples.petclinic.model.veterinarian;

import io.vlingo.xoom.examples.petclinic.model.*;
import io.vlingo.xoom.common.Completes;

import io.vlingo.xoom.lattice.model.sourcing.EventSourced;

/**
 * See <a href="https://docs.vlingo.io/xoom-lattice/entity-cqrs#sourced">EventSourced</a>
 */
public final class VeterinarianEntity extends EventSourced implements Veterinarian {
  private VeterinarianState state;

  public VeterinarianEntity(final String id) {
    super(id);
    this.state = VeterinarianState.identifiedBy(id);
  }

  static {
    EventSourced.registerConsumer(VeterinarianEntity.class, VeterinarianRegistered.class, VeterinarianEntity::applyVeterinarianRegistered);
    EventSourced.registerConsumer(VeterinarianEntity.class, VeterinarianContactInformationChanged.class, VeterinarianEntity::applyVeterinarianContactInformationChanged);
    EventSourced.registerConsumer(VeterinarianEntity.class, VeterinarianNameChanged.class, VeterinarianEntity::applyVeterinarianNameChanged);
    EventSourced.registerConsumer(VeterinarianEntity.class, VeterinarianSpecialtyChosen.class, VeterinarianEntity::applyVeterinarianSpecialtyChosen);
  }

  @Override
  public Completes<VeterinarianState> register(final FullName name, final ContactInformation contactInformation, final Specialty specialty) {
    final VeterinarianState stateArg = state.register(name, contactInformation, specialty);
    return apply(new VeterinarianRegistered(stateArg), () -> state);
  }

  @Override
  public Completes<VeterinarianState> changeContactInformation(final ContactInformation contactInformation) {
    final VeterinarianState stateArg = state.changeContactInformation(contactInformation);
    return apply(new VeterinarianContactInformationChanged(stateArg), () -> state);
  }

  @Override
  public Completes<VeterinarianState> changeName(final FullName name) {
    final VeterinarianState stateArg = state.changeName(name);
    return apply(new VeterinarianNameChanged(stateArg), () -> state);
  }

  @Override
  public Completes<VeterinarianState> specializesIn(final Specialty specialty) {
    final VeterinarianState stateArg = state.specializesIn(specialty);
    return apply(new VeterinarianSpecialtyChosen(stateArg), () -> state);
  }

  private void applyVeterinarianRegistered(final VeterinarianRegistered event) {
    state = state.register(event.name, event.contactInformation, event.specialty);
  }

  private void applyVeterinarianContactInformationChanged(final VeterinarianContactInformationChanged event) {
    state = state.changeContactInformation(event.contactInformation);
  }

  private void applyVeterinarianNameChanged(final VeterinarianNameChanged event) {
    state = state.changeName(event.name);
  }

  private void applyVeterinarianSpecialtyChosen(final VeterinarianSpecialtyChosen event) {
    state = state.specializesIn(event.specialty);
  }

  /*
   * Restores my initial state by means of {@code state}.
   *
   * @param snapshot the {@code VeterinarianState} holding my state
   * @param currentVersion the int value of my current version; may be helpful in determining if snapshot is needed
   */
  @Override
  protected <VeterinarianState> void restoreSnapshot(final VeterinarianState snapshot, final int currentVersion) {
    // OVERRIDE FOR SNAPSHOT SUPPORT
    // See: https://docs.vlingo.io/xoom-lattice/entity-cqrs#eventsourced
  }

  /*
   * Answer the valid {@code VeterinarianState} instance if a snapshot should
   * be taken and persisted along with applied {@code Source<T>} instance(s).
   *
   * @return VeterinarianState
   */
  @Override
  protected VeterinarianState snapshot() {
    // OVERRIDE FOR SNAPSHOT SUPPORT
    // See: https://docs.vlingo.io/xoom-lattice/entity-cqrs#eventsourced
    return null;
  }
}
