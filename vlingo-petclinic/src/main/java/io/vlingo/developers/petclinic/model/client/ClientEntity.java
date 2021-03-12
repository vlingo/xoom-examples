package io.vlingo.developers.petclinic.model.client;

import io.vlingo.developers.petclinic.model.ContactInformation;
import io.vlingo.developers.petclinic.model.Fullname;
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

  public Completes<ClientState> register(final Fullname name, final ContactInformation contact) {
    return apply(new ClientRegistered(state.id, name, contact), () -> state);
  }

  public Completes<ClientState> changeName(final Fullname name) {
    return apply(new ClientNameChanged(name), () -> state);
  }

  public Completes<ClientState> changeContactInformation(final ContactInformation contact) {
    return apply(new ClientContactInformationChanged(contact), () -> state);
  }

  private void applyClientRegistered(final ClientRegistered event) {
    this.state = state.register(event.name, event.contact);
  }

  private void applyClientNameChanged(final ClientNameChanged event) {
    this.state = state.changeName(event.name);
  }

  private void applyClientContactInformationChanged(final ClientContactInformationChanged event) {
    this.state = state.changeContactInformation(event.contact);
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
