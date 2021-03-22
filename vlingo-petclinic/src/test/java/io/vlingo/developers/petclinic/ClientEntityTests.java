package io.vlingo.developers.petclinic;

import io.vlingo.actors.World;
import io.vlingo.actors.testkit.AccessSafely;
import io.vlingo.developers.petclinic.infrastructure.persistence.ClientContactInformationChangedAdapter;
import io.vlingo.developers.petclinic.infrastructure.persistence.ClientNameChangedAdapter;
import io.vlingo.developers.petclinic.infrastructure.persistence.ClientRegisteredAdapter;
import io.vlingo.developers.petclinic.model.ContactInformation;
import io.vlingo.developers.petclinic.model.Fullname;
import io.vlingo.developers.petclinic.model.PostalAddress;
import io.vlingo.developers.petclinic.model.Telephone;
import io.vlingo.developers.petclinic.model.client.*;
import io.vlingo.lattice.model.sourcing.SourcedTypeRegistry;
import io.vlingo.symbio.BaseEntry;
import io.vlingo.symbio.EntryAdapterProvider;
import io.vlingo.symbio.store.journal.Journal;
import io.vlingo.symbio.store.journal.inmemory.InMemoryJournalActor;
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
    public void setUp(){
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
    public void register(){
        final AccessSafely dispatcherAccess = dispatcher.afterCompleting(1);

        final ContactInformation contact = ContactInformation.of(
                PostalAddress.of("St.", "London", "UK", "123"),
                Telephone.of("112233")
        );
        final ClientState clientState = client.register(Fullname.of("Harry", "Potter"), contact).await();

        assertNotNull(clientState);
        assertEquals("#1", clientState.id);
        assertNotNull(clientState.name);
        assertEquals("Harry", clientState.name.first);
        assertEquals("Potter", clientState.name.last);
        assertNotNull(clientState.contact);
        assertNotNull(clientState.contact.postalAddress);
        assertNotNull(clientState.contact.telephone);
        assertEquals("St.", clientState.contact.postalAddress.streetAddress);
        assertEquals("London", clientState.contact.postalAddress.city);
        assertEquals("UK", clientState.contact.postalAddress.stateProvince);
        assertEquals("123", clientState.contact.postalAddress.postalCode);
        assertEquals("112233", clientState.contact.telephone.number);

        // this will block until the first event is persisted in the Journal
        assertEquals(1, (int) dispatcherAccess.readFrom("entriesCount"));
        BaseEntry<String> appendedAt0 = dispatcherAccess.readFrom("appendedAt", 0);
        assertNotNull(appendedAt0);
        assertEquals(ClientRegistered.class.getName(), appendedAt0.typeName());
    }

    private ClientState registerExampleClient(){
        final ContactInformation contact = ContactInformation.of(
                PostalAddress.of("St.", "London", "UK", "123"),
                Telephone.of("112233")
        );
        return client.register(Fullname.of("Harry", "Potter"), contact).await();
    }

    @Test
    public void rename(){
        registerExampleClient();

        final AccessSafely dispatcherAccess = dispatcher.afterCompleting(1);

        ClientState clientState = client.changeName(Fullname.of("Severus", "Snape")).await();

        assertNotNull(clientState);
        assertEquals("#1", clientState.id);
        assertNotNull(clientState.name);
        assertEquals("Severus", clientState.name.first);
        assertEquals("Snape", clientState.name.last);
        assertNotNull(clientState.contact);
        assertNotNull(clientState.contact.postalAddress);
        assertNotNull(clientState.contact.telephone);
        assertEquals("St.", clientState.contact.postalAddress.streetAddress);
        assertEquals("London", clientState.contact.postalAddress.city);
        assertEquals("UK", clientState.contact.postalAddress.stateProvince);
        assertEquals("123", clientState.contact.postalAddress.postalCode);
        assertEquals("112233", clientState.contact.telephone.number);

        // this will block until the first event is persisted in the Journal
        assertEquals(2, (int) dispatcherAccess.readFrom("entriesCount"));
        BaseEntry<String> appendedAt1 = dispatcherAccess.readFrom("appendedAt", 1);
        assertNotNull(appendedAt1);
        assertEquals(ClientNameChanged.class.getName(), appendedAt1.typeName());
    }

    @Test
    public void contactUpdate(){
        registerExampleClient();

        final AccessSafely dispatcherAccess = dispatcher.afterCompleting(1);

        final ContactInformation contact = ContactInformation.of(
                PostalAddress.of("Ave.", "New-York", "US", "321"),
                Telephone.of("991100")
        );
        ClientState clientState = client.changeContactInformation(contact).await();

        assertNotNull(clientState);
        assertEquals("#1", clientState.id);
        assertNotNull(clientState.name);
        assertEquals("Harry", clientState.name.first);
        assertEquals("Potter", clientState.name.last);
        assertNotNull(clientState.contact);
        assertNotNull(clientState.contact.postalAddress);
        assertNotNull(clientState.contact.telephone);
        assertEquals("Ave.", clientState.contact.postalAddress.streetAddress);
        assertEquals("New-York", clientState.contact.postalAddress.city);
        assertEquals("US", clientState.contact.postalAddress.stateProvince);
        assertEquals("321", clientState.contact.postalAddress.postalCode);
        assertEquals("991100", clientState.contact.telephone.number);

        // this will block until the first event is persisted in the Journal
        assertEquals(2, (int) dispatcherAccess.readFrom("entriesCount"));
        BaseEntry<String> appendedAt1 = dispatcherAccess.readFrom("appendedAt", 1);
        assertNotNull(appendedAt1);
        assertEquals(ClientContactInformationChanged.class.getName(), appendedAt1.typeName());
    }
}
