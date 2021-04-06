package io.vlingo.xoom.petclinic.model.client;

import io.vlingo.xoom.petclinic.model.*;
import io.vlingo.common.Completes;

import io.vlingo.lattice.model.sourcing.EventSourced;

/**
 * See <a href="https://docs.vlingo.io/vlingo-lattice/entity-cqrs#sourced">EventSourced</a>
 */
public final class ClientEntity extends EventSourced implements Client {
  private ClientState state;

  public ClientEntity(final String id) {
    super(id);
    this.state = ClientState.identifiedBy(id);
  }

  static {
    EventSourced.registerConsumer(ClientEntity.class, ClientRegistered.class, ClientEntity::applyClientRegistered);
    EventSourced.registerConsumer(ClientEntity.class, ClientNameChanged.class, ClientEntity::applyClientNameChanged);
    EventSourced.registerConsumer(ClientEntity.class, ClientContactInformationChanged.class, ClientEntity::applyClientContactInformationChanged);
  }

  @Override
  public Completes<ClientState> register(final FullName name, final ContactInformation contactInformation) {
    final ClientState stateArg = state.register(name, contactInformation);
    return apply(new ClientRegistered(stateArg), () -> state);
  }

  @Override
  public Completes<ClientState> changeContactInformation(final ContactInformation contactInformation) {
    final ClientState stateArg = state.changeContactInformation(contactInformation);
    return apply(new ClientContactInformationChanged(stateArg), () -> state);
  }

  @Override
  public Completes<ClientState> changeName(final FullName name) {
    final ClientState stateArg = state.changeName(name);
    return apply(new ClientNameChanged(stateArg), () -> state);
  }

  private void applyClientRegistered(final ClientRegistered event) {
    state = state.register(event.name, event.contactInformation);
  }

  private void applyClientContactInformationChanged(final ClientContactInformationChanged event) {
    state = state.changeContactInformation(event.contactInformation);
  }

  private void applyClientNameChanged(final ClientNameChanged event) {
    state = state.changeName(event.name);
  }

  /*
   * Restores my initial state by means of {@code state}.
   *
   * @param snapshot the {@code ClientState} holding my state
   * @param currentVersion the int value of my current version; may be helpful in determining if snapshot is needed
   */
  @Override
  protected <ClientState> void restoreSnapshot(final ClientState snapshot, final int currentVersion) {
    // OVERRIDE FOR SNAPSHOT SUPPORT
    // See: https://docs.vlingo.io/vlingo-lattice/entity-cqrs#eventsourced
  }

  /*
   * Answer the valid {@code ClientState} instance if a snapshot should
   * be taken and persisted along with applied {@code Source<T>} instance(s).
   *
   * @return ClientState
   */
  @Override
  protected ClientState snapshot() {
    // OVERRIDE FOR SNAPSHOT SUPPORT
    // See: https://docs.vlingo.io/vlingo-lattice/entity-cqrs#eventsourced
    return null;
  }
}
