package io.vlingo.xoom.examples.petclinic;

import io.vlingo.xoom.actors.World;
import io.vlingo.xoom.actors.testkit.AccessSafely;
import io.vlingo.xoom.examples.petclinic.infrastructure.persistence.VeterinarianNameChangedAdapter;
import io.vlingo.xoom.examples.petclinic.infrastructure.persistence.VeterinarianRegisteredAdapter;
import io.vlingo.xoom.examples.petclinic.model.*;
import io.vlingo.xoom.examples.petclinic.model.veterinarian.*;
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

public class VeterinarianEntityTests {

  private World world;

  private Journal<String> journal;
  private MockJournalDispatcher dispatcher;
  private SourcedTypeRegistry registry;

  private Veterinarian vet;

  @BeforeEach
  @SuppressWarnings({"unchecked", "rawtypes"})
  public void setUp() {
    world = World.startWithDefaults("test-es");

    dispatcher = new MockJournalDispatcher();

    EntryAdapterProvider entryAdapterProvider = EntryAdapterProvider.instance(world);

    entryAdapterProvider.registerAdapter(VeterinarianRegistered.class, new VeterinarianRegisteredAdapter());
    entryAdapterProvider.registerAdapter(VeterinarianNameChanged.class, new VeterinarianNameChangedAdapter());

    journal = world.actorFor(Journal.class, InMemoryJournalActor.class, Collections.singletonList(dispatcher));

    registry = new SourcedTypeRegistry(world);
    registry.register(new SourcedTypeRegistry.Info(journal, VeterinarianEntity.class, VeterinarianEntity.class.getSimpleName()));

    vet = world.actorFor(Veterinarian.class, VeterinarianEntity.class, "#1");
  }

  @Test
  public void offerByFactoryMethod() {
    final ContactInformation contact = ContactInformation.from(
        PostalAddress.from("Ave.", "New-York", "US", "321"),
        Telephone.from("991100")
    );
    VeterinarianState vetState = Veterinarian.register(world.stage(), FullName.from("Queenie", "Goldstein"),
        contact, Specialty.from("Behaviour")).await();
    assertNotNull(vetState);
    assertNotNull(vetState.name);
    assertNotNull(vetState.contactInformation);
    assertNotNull(vetState.contactInformation.postalAddress);
    assertNotNull(vetState.contactInformation.telephone);
    assertNotNull(vetState.specialty);
    assertNotNull(vetState.id);
    assertEquals("Queenie", vetState.name.first);
    assertEquals("Goldstein", vetState.name.last);

    assertEquals("Ave.", vetState.contactInformation.postalAddress.streetAddress);
    assertEquals("New-York", vetState.contactInformation.postalAddress.city);
    assertEquals("US", vetState.contactInformation.postalAddress.stateProvince);
    assertEquals("321", vetState.contactInformation.postalAddress.postalCode);
    assertEquals("991100", vetState.contactInformation.telephone.number);

    assertEquals("Behaviour", vetState.specialty.specialtyTypeId);
  }

  @Test
  public void offer() {
    final AccessSafely dispatcherAccess = dispatcher.afterCompleting(1);

    final ContactInformation contact = ContactInformation.from(
        PostalAddress.from("Ave.", "New-York", "US", "321"),
        Telephone.from("991100")
    );
    VeterinarianState vetState = vet.register(FullName.from("Rubeus", "Hagrid"), contact, Specialty.from("Behaviour")).await();

    assertNotNull(vetState);
    assertNotNull(vetState.name);
    assertNotNull(vetState.contactInformation);
    assertNotNull(vetState.contactInformation.postalAddress);
    assertNotNull(vetState.contactInformation.telephone);
    assertNotNull(vetState.specialty);
    assertEquals("#1", vetState.id);
    assertEquals("Rubeus", vetState.name.first);
    assertEquals("Hagrid", vetState.name.last);

    assertEquals("Ave.", vetState.contactInformation.postalAddress.streetAddress);
    assertEquals("New-York", vetState.contactInformation.postalAddress.city);
    assertEquals("US", vetState.contactInformation.postalAddress.stateProvince);
    assertEquals("321", vetState.contactInformation.postalAddress.postalCode);
    assertEquals("991100", vetState.contactInformation.telephone.number);

    assertEquals("Behaviour", vetState.specialty.specialtyTypeId);

    // this will block until the first event is persisted in the Journal
    assertEquals(1, (int) dispatcherAccess.readFrom("entriesCount"));
    BaseEntry<String> appendedAt0 = dispatcherAccess.readFrom("appendedAt", 0);
    assertNotNull(appendedAt0);
    assertEquals(VeterinarianRegistered.class.getName(), appendedAt0.typeName());
  }

  private VeterinarianState offerExampleVet() {
    final ContactInformation contact = ContactInformation.from(
        PostalAddress.from("Ave.", "New-York", "US", "321"),
        Telephone.from("991100")
    );
    return vet.register(FullName.from("Rubeus", "Hagrid"), contact, Specialty.from("Behaviour")).await();
  }

  @Test
  public void rename() {
    offerExampleVet();

    final AccessSafely dispatcherAccess = dispatcher.afterCompleting(1);

    VeterinarianState vetState = vet.changeName(FullName.from("Albus", "Dumbledore")).await();

    assertEquals("#1", vetState.id);
    assertEquals("Albus", vetState.name.first);
    assertEquals("Dumbledore", vetState.name.last);

    // this will block until the first event is persisted in the Journal
    assertEquals(2, (int) dispatcherAccess.readFrom("entriesCount"));
    BaseEntry<String> appendedAt1 = dispatcherAccess.readFrom("appendedAt", 1);
    assertNotNull(appendedAt1);
    assertEquals(VeterinarianNameChanged.class.getName(), appendedAt1.typeName());
  }

  @Test
  public void specialize() {
    offerExampleVet();

    final AccessSafely dispatcherAccess = dispatcher.afterCompleting(1);

    VeterinarianState vetState = vet.specializesIn(Specialty.from("Ophthalmology")).await();

    assertEquals("#1", vetState.id);
    assertEquals("Ophthalmology", vetState.specialty.specialtyTypeId);

    // this will block until the first event is persisted in the Journal
    assertEquals(2, (int) dispatcherAccess.readFrom("entriesCount"));
    BaseEntry<String> appendedAt1 = dispatcherAccess.readFrom("appendedAt", 1);
    assertNotNull(appendedAt1);
    assertEquals(VeterinarianSpecialtyChosen.class.getName(), appendedAt1.typeName());
  }

  @Test
  public void contactUpdate() {
    offerExampleVet();

    final AccessSafely dispatcherAccess = dispatcher.afterCompleting(1);

    final ContactInformation contact = ContactInformation.from(
        PostalAddress.from("Ave.", "New-York", "US", "321"),
        Telephone.from("991100")
    );
    VeterinarianState vetState = vet.changeContactInformation(contact).await();

    assertEquals("#1", vetState.id);
    assertEquals("Ave.", vetState.contactInformation.postalAddress.streetAddress);
    assertEquals("New-York", vetState.contactInformation.postalAddress.city);
    assertEquals("US", vetState.contactInformation.postalAddress.stateProvince);
    assertEquals("321", vetState.contactInformation.postalAddress.postalCode);
    assertEquals("991100", vetState.contactInformation.telephone.number);

    // this will block until the first event is persisted in the Journal
    assertEquals(2, (int) dispatcherAccess.readFrom("entriesCount"));
    BaseEntry<String> appendedAt1 = dispatcherAccess.readFrom("appendedAt", 1);
    assertNotNull(appendedAt1);
    assertEquals(VeterinarianContactInformationChanged.class.getName(), appendedAt1.typeName());
  }
}
