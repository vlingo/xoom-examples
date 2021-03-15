package io.vlingo.developers.petclinic.model.veterinarian;

import io.vlingo.developers.petclinic.model.ContactInformation;
import io.vlingo.developers.petclinic.model.client.Specialty;
import io.vlingo.developers.petclinic.model.Fullname;
import io.vlingo.common.Completes;

import io.vlingo.lattice.model.sourcing.EventSourced;

/**
 * See <a href="https://docs.vlingo.io/vlingo-lattice/entity-cqrs#sourced">EventSourced</a>
 */
public final class VeterinarianEntity extends EventSourced implements Veterinarian {
  private VeterinarianState state;

  public VeterinarianEntity(final String id) {
    super(id);
    this.state = VeterinarianState.identifiedBy(id);
  }

  static {
    EventSourced.registerConsumer(VeterinarianEntity.class, VeterinarianRegistered.class, VeterinarianEntity::applyVeterinarianRegistered);
    EventSourced.registerConsumer(VeterinarianEntity.class, VeterinarianNameChanged.class, VeterinarianEntity::applyVeterinarianNameChanged);
    EventSourced.registerConsumer(VeterinarianEntity.class, VeterinarianContactInformationChanged.class, VeterinarianEntity::applyVeterinarianContactInformationChanged);
    EventSourced.registerConsumer(VeterinarianEntity.class, VeterinarianSpecialtyChosen.class, VeterinarianEntity::applyVeterinarianSpecialtyChosen);
  }

  public Completes<VeterinarianState> register(final Fullname name, final ContactInformation contact, final Specialty specialty) {
    return apply(new VeterinarianRegistered(state.id, name, contact, specialty), () -> state);
  }

  public Completes<VeterinarianState> changeName(final Fullname name) {
    return apply(new VeterinarianNameChanged(state.id, name), () -> state);
  }

  public Completes<VeterinarianState> changeContactInformation(final ContactInformation contact) {
    return apply(new VeterinarianContactInformationChanged(state.id, contact), () -> state);
  }

  public Completes<VeterinarianState> specializesIn(final Specialty specialty) {
    return apply(new VeterinarianSpecialtyChosen(state.id, specialty), () -> state);
  }

  private void applyVeterinarianRegistered(final VeterinarianRegistered event) {
    this.state = state.register(event.name, event.contact, event.specialty);
  }

  private void applyVeterinarianNameChanged(final VeterinarianNameChanged event) {
    this.state = state.changeName(event.name);
  }

  private void applyVeterinarianContactInformationChanged(final VeterinarianContactInformationChanged event) {
    this.state = state.changeContactInformation(event.contact);
  }

  private void applyVeterinarianSpecialtyChosen(final VeterinarianSpecialtyChosen event) {
    this.state = state.specializesIn(event.specialty);
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
    // See: https://docs.vlingo.io/vlingo-lattice/entity-cqrs#eventsourced
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
    // See: https://docs.vlingo.io/vlingo-lattice/entity-cqrs#eventsourced
    return null;
  }
}
