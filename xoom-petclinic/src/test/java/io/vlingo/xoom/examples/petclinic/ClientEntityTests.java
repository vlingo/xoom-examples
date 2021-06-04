package io.vlingo.xoom.examples.petclinic;

import io.vlingo.xoom.actors.World;
import io.vlingo.xoom.actors.testkit.AccessSafely;
import io.vlingo.xoom.examples.petclinic.infrastructure.persistence.ClientContactInformationChangedAdapter;
import io.vlingo.xoom.examples.petclinic.infrastructure.persistence.ClientNameChangedAdapter;
import io.vlingo.xoom.examples.petclinic.infrastructure.persistence.ClientRegisteredAdapter;
import io.vlingo.xoom.examples.petclinic.model.ContactInformation;
import io.vlingo.xoom.examples.petclinic.model.FullName;
import io.vlingo.xoom.examples.petclinic.model.PostalAddress;
import io.vlingo.xoom.examples.petclinic.model.Telephone;
import io.vlingo.xoom.examples.petclinic.model.client.*;
import io.vlingo.xoom.lattice.model.sourcing.SourcedTypeRegistry;
import io.vlingo.xoom.symbio.BaseEntry;
import io.vlingo.xoom.symbio.EntryAdapterProvider;
import io.vlingo.xoom.symbio.store.journal.Journal;
import io.vlingo.xoom.symbio.store.journal.inmemory.InMemoryJournalActor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ClientEntityTests {

  private World world;

  private Journal<String> journal;
  private MockJournalDispatcher dispatcher;
  private SourcedTypeRegistry registry;

  private Client client;

  @BeforeEach
  @SuppressWarnings({"unchecked", "rawtypes"})
  public void setUp() {
    world = World.startWithDefaults("test-es");

    dispatcher = new MockJournalDispatcher();

    EntryAdapterProvider entryAdapterProvider = EntryAdapterProvider.instance(world);

    entryAdapterProvider.registerAdapter(ClientRegistered.class, new ClientRegisteredAdapter());
    entryAdapterProvider.registerAdapter(ClientNameChanged.class, new ClientNameChangedAdapter());
    entryAdapterProvider.registerAdapter(ClientContactInformationChanged.class, new ClientContactInformationChangedAdapter());

    journal = world.actorFor(Journal.class, InMemoryJournalActor.class, Collections.singletonList(dispatcher));

    registry = new SourcedTypeRegistry(world);
    registry.register(new SourcedTypeRegistry.Info(journal, ClientEntity.class, ClientEntity.class.getSimpleName()));

    client = world.actorFor(Client.class, ClientEntity.class, "#1");
  }

  @Test
  public void registerByFactoryMethod() {
    final ContactInformation contact = ContactInformation.from(
        PostalAddress.from("St.", "London", "UK", "123"),
        Telephone.from("112233")
    );
    ClientState clientState = Client.register(world.stage(), FullName.from("Newt", "Scamander"), contact).await();

    assertNotNull(clientState);
    assertNotNull(clientState.id);
    assertNotNull(clientState.name);
    assertEquals("Newt", clientState.name.first);
    assertEquals("Scamander", clientState.name.last);
    assertNotNull(clientState.contactInformation);
    assertNotNull(clientState.contactInformation.postalAddress);
    assertNotNull(clientState.contactInformation.telephone);
    assertEquals("St.", clientState.contactInformation.postalAddress.streetAddress);
    assertEquals("London", clientState.contactInformation.postalAddress.city);
    assertEquals("UK", clientState.contactInformation.postalAddress.stateProvince);
    assertEquals("123", clientState.contactInformation.postalAddress.postalCode);
    assertEquals("112233", clientState.contactInformation.telephone.number);
  }

  @Test
  public void register() {
    final AccessSafely dispatcherAccess = dispatcher.afterCompleting(1);

    final ContactInformation contact = ContactInformation.from(
        PostalAddress.from("St.", "London", "UK", "123"),
        Telephone.from("112233")
    );
    final ClientState clientState = client.register(FullName.from("Harry", "Potter"), contact).await();

    assertNotNull(clientState);
    assertEquals("#1", clientState.id);
    assertNotNull(clientState.name);
    assertEquals("Harry", clientState.name.first);
    assertEquals("Potter", clientState.name.last);
    assertNotNull(clientState.contactInformation);
    assertNotNull(clientState.contactInformation.postalAddress);
    assertNotNull(clientState.contactInformation.telephone);
    assertEquals("St.", clientState.contactInformation.postalAddress.streetAddress);
    assertEquals("London", clientState.contactInformation.postalAddress.city);
    assertEquals("UK", clientState.contactInformation.postalAddress.stateProvince);
    assertEquals("123", clientState.contactInformation.postalAddress.postalCode);
    assertEquals("112233", clientState.contactInformation.telephone.number);

    // this will block until the first event is persisted in the Journal
    assertEquals(1, (int) dispatcherAccess.readFrom("entriesCount"));
    BaseEntry<String> appendedAt0 = dispatcherAccess.readFrom("appendedAt", 0);
    assertNotNull(appendedAt0);
    assertEquals(ClientRegistered.class.getName(), appendedAt0.typeName());
  }

  private ClientState registerExampleClient() {
    final ContactInformation contact = ContactInformation.from(
        PostalAddress.from("St.", "London", "UK", "123"),
        Telephone.from("112233")
    );
    return client.register(FullName.from("Harry", "Potter"), contact).await();
  }

  @Test
  public void rename() {
    registerExampleClient();

    final AccessSafely dispatcherAccess = dispatcher.afterCompleting(1);

    ClientState clientState = client.changeName(FullName.from("Severus", "Snape")).await();

    assertNotNull(clientState);
    assertEquals("#1", clientState.id);
    assertNotNull(clientState.name);
    assertEquals("Severus", clientState.name.first);
    assertEquals("Snape", clientState.name.last);
    assertNotNull(clientState.contactInformation);
    assertNotNull(clientState.contactInformation.postalAddress);
    assertNotNull(clientState.contactInformation.telephone);
    assertEquals("St.", clientState.contactInformation.postalAddress.streetAddress);
    assertEquals("London", clientState.contactInformation.postalAddress.city);
    assertEquals("UK", clientState.contactInformation.postalAddress.stateProvince);
    assertEquals("123", clientState.contactInformation.postalAddress.postalCode);
    assertEquals("112233", clientState.contactInformation.telephone.number);

    // this will block until the first event is persisted in the Journal
    assertEquals(2, (int) dispatcherAccess.readFrom("entriesCount"));
    BaseEntry<String> appendedAt1 = dispatcherAccess.readFrom("appendedAt", 1);
    assertNotNull(appendedAt1);
    assertEquals(ClientNameChanged.class.getName(), appendedAt1.typeName());
  }

  @Test
  public void contactUpdate() {
    registerExampleClient();

    final AccessSafely dispatcherAccess = dispatcher.afterCompleting(1);

    final ContactInformation contact = ContactInformation.from(
        PostalAddress.from("Ave.", "New-York", "US", "321"),
        Telephone.from("991100")
    );
    ClientState clientState = client.changeContactInformation(contact).await();

    assertNotNull(clientState);
    assertEquals("#1", clientState.id);
    assertNotNull(clientState.name);
    assertEquals("Harry", clientState.name.first);
    assertEquals("Potter", clientState.name.last);
    assertNotNull(clientState.contactInformation);
    assertNotNull(clientState.contactInformation.postalAddress);
    assertNotNull(clientState.contactInformation.telephone);
    assertEquals("Ave.", clientState.contactInformation.postalAddress.streetAddress);
    assertEquals("New-York", clientState.contactInformation.postalAddress.city);
    assertEquals("US", clientState.contactInformation.postalAddress.stateProvince);
    assertEquals("321", clientState.contactInformation.postalAddress.postalCode);
    assertEquals("991100", clientState.contactInformation.telephone.number);

    // this will block until the first event is persisted in the Journal
    assertEquals(2, (int) dispatcherAccess.readFrom("entriesCount"));
    BaseEntry<String> appendedAt1 = dispatcherAccess.readFrom("appendedAt", 1);
    assertNotNull(appendedAt1);
    assertEquals(ClientContactInformationChanged.class.getName(), appendedAt1.typeName());
  }
}
