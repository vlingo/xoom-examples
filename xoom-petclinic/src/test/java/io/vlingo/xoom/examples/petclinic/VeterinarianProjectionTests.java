package io.vlingo.xoom.examples.petclinic;

import io.vlingo.xoom.actors.World;
import io.vlingo.xoom.actors.testkit.AccessSafely;
import io.vlingo.xoom.common.serialization.JsonSerialization;
import io.vlingo.xoom.examples.petclinic.infrastructure.VeterinarianData;
import io.vlingo.xoom.examples.petclinic.infrastructure.persistence.QueryModelStateStoreProvider;
import io.vlingo.xoom.examples.petclinic.infrastructure.persistence.VeterinarianProjectionActor;
import io.vlingo.xoom.examples.petclinic.model.*;
import io.vlingo.xoom.examples.petclinic.model.veterinarian.*;
import io.vlingo.xoom.lattice.model.projection.Projectable;
import io.vlingo.xoom.lattice.model.projection.Projection;
import io.vlingo.xoom.lattice.model.projection.TextProjectable;
import io.vlingo.xoom.lattice.model.stateful.StatefulTypeRegistry;
import io.vlingo.xoom.symbio.BaseEntry;
import io.vlingo.xoom.symbio.Metadata;
import io.vlingo.xoom.symbio.store.dispatch.NoOpDispatcher;
import io.vlingo.xoom.symbio.store.state.StateStore;
import io.vlingo.xoom.symbio.store.state.inmemory.InMemoryStateStoreActor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VeterinarianProjectionTests {

  private World world;
  private StateStore stateStore;
  private Projection projection;
  private Map<String, String> valueToProjectionId;

  @BeforeEach
  public void setUp() {
    world = World.startWithDefaults("test-state-store-projection");
    NoOpDispatcher dispatcher = new NoOpDispatcher();
    valueToProjectionId = new ConcurrentHashMap<>();
    stateStore = world.actorFor(StateStore.class, InMemoryStateStoreActor.class, Collections.singletonList(dispatcher));
    StatefulTypeRegistry statefulTypeRegistry = StatefulTypeRegistry.registerAll(world, stateStore, VeterinarianData.class);
    QueryModelStateStoreProvider.using(world.stage(), statefulTypeRegistry);
    projection = world.actorFor(Projection.class, VeterinarianProjectionActor.class, stateStore);
  }

  private Projectable createVetRegistered(String id, FullName name, ContactInformation contact, String specialty) {
    final VeterinarianRegistered eventData = new VeterinarianRegistered(new VeterinarianState(id, name, contact, Specialty.from(specialty)));

    BaseEntry.TextEntry textEntry = new BaseEntry.TextEntry(VeterinarianRegistered.class, 1,
        JsonSerialization.serialized(eventData), 1, Metadata.withObject(eventData));

    final String projectionId = UUID.randomUUID().toString();
    valueToProjectionId.put(id, projectionId);
    return new TextProjectable(null, Collections.singletonList(textEntry), projectionId);
  }

  @Test
  public void register() {
    final CountingProjectionControl control = new CountingProjectionControl();
    final AccessSafely access = control.afterCompleting(2);

    final ContactInformation contact = ContactInformation.from(
        PostalAddress.from("Ave.", "New-York", "US", "321"),
        Telephone.from("991100")
    );
    final ContactInformation contact2 = ContactInformation.from(
        PostalAddress.from("Seoul St.", "UB", "MN", "976"),
        Telephone.from("981908")
    );
    projection.projectWith(createVetRegistered("1", FullName.from("Albus", "Dumbledore"), contact, "surgery"), control);
    projection.projectWith(createVetRegistered("2", FullName.from("Rubeus", "Hagrid"), contact2, "Behaviour"), control);

    final Map<String, Integer> confirmations = access.readFrom("confirmations");

    assertEquals(2, confirmations.size());

    assertEquals(1, valueOfProjectionIdFor("1", confirmations));
    assertEquals(1, valueOfProjectionIdFor("2", confirmations));

    CountingReadResultInterest interest = new CountingReadResultInterest();
    AccessSafely interestAccess = interest.afterCompleting(1);
    stateStore.read("1", VeterinarianData.class, interest);
    VeterinarianData vet = interestAccess.readFrom("item", "1");
    assertEquals("1", vet.id);
    assertEquals("Albus", vet.name.first);
    assertEquals("Dumbledore", vet.name.last);
    assertEquals("991100", vet.contactInformation.telephone.number);
    assertEquals("New-York", vet.contactInformation.postalAddress.city);
    assertEquals("Ave.", vet.contactInformation.postalAddress.streetAddress);
    assertEquals("US", vet.contactInformation.postalAddress.stateProvince);

    interest = new CountingReadResultInterest();
    interestAccess = interest.afterCompleting(1);
    stateStore.read("2", VeterinarianData.class, interest);
    vet = interestAccess.readFrom("item", "2");
    assertEquals("2", vet.id);
    assertEquals("Rubeus", vet.name.first);
    assertEquals("Hagrid", vet.name.last);
    assertEquals("981908", vet.contactInformation.telephone.number);
    assertEquals("UB", vet.contactInformation.postalAddress.city);
    assertEquals("Seoul St.", vet.contactInformation.postalAddress.streetAddress);
    assertEquals("MN", vet.contactInformation.postalAddress.stateProvince);
  }

  private void registerExampleVets() {
    final CountingProjectionControl control = new CountingProjectionControl();
    final AccessSafely access = control.afterCompleting(2);
    final ContactInformation contact = ContactInformation.from(
        PostalAddress.from("Ave.", "New-York", "US", "321"),
        Telephone.from("991100")
    );
    final ContactInformation contact2 = ContactInformation.from(
        PostalAddress.from("Seoul St.", "UB", "MN", "976"),
        Telephone.from("981908")
    );
    projection.projectWith(createVetRegistered("1", FullName.from("Albus", "Dumbldore"), contact, "surgery"), control);
    projection.projectWith(createVetRegistered("2", FullName.from("Rubeus", "Hagrid"), contact2, "Behaviour"), control);
  }

  @Test
  public void changeName() {
    registerExampleVets();

    final CountingProjectionControl control = new CountingProjectionControl();
    final AccessSafely access = control.afterCompleting(1);

    final VeterinarianNameChanged eventData =
        new VeterinarianNameChanged(new VeterinarianState("1", FullName.from("Severus", "Snape"), null, null));
    BaseEntry.TextEntry textEntry = new BaseEntry.TextEntry(VeterinarianNameChanged.class, 1,
        JsonSerialization.serialized(eventData), 2, Metadata.withObject(eventData));
    final String projectionId = UUID.randomUUID().toString();
    valueToProjectionId.put("1", projectionId);
    Projectable p = new TextProjectable(null, Collections.singletonList(textEntry), projectionId);

    projection.projectWith(p, control);

    final Map<String, Integer> confirmations = access.readFrom("confirmations");

    assertEquals(1, confirmations.size());
    assertEquals(1, valueOfProjectionIdFor("1", confirmations));

    final CountingReadResultInterest interest = new CountingReadResultInterest();
    final AccessSafely interestAccess = interest.afterCompleting(1);

    stateStore.read("1", VeterinarianData.class, interest);

    final VeterinarianData vet = interestAccess.readFrom("item", "1");
    assertEquals("1", vet.id);
    assertEquals("Severus", vet.name.first);
    assertEquals("Snape", vet.name.last);
  }

  @Test
  public void changeContact() {
    registerExampleVets();

    final CountingProjectionControl control = new CountingProjectionControl();
    final AccessSafely access = control.afterCompleting(1);

    final ContactInformation contact = ContactInformation.from(
        PostalAddress.from("St.", "London", "UK", "991"),
        Telephone.from("112233")
    );
    final VeterinarianContactInformationChanged eventData =
        new VeterinarianContactInformationChanged(new VeterinarianState("1", null, contact, null));
    BaseEntry.TextEntry textEntry = new BaseEntry.TextEntry(VeterinarianContactInformationChanged.class, 1,
        JsonSerialization.serialized(eventData), 2, Metadata.withObject(eventData));
    final String projectionId = UUID.randomUUID().toString();
    valueToProjectionId.put("1", projectionId);
    Projectable p = new TextProjectable(null, Collections.singletonList(textEntry), projectionId);

    projection.projectWith(p, control);

    final Map<String, Integer> confirmations = access.readFrom("confirmations");

    assertEquals(1, confirmations.size());
    assertEquals(1, valueOfProjectionIdFor("1", confirmations));

    final CountingReadResultInterest interest = new CountingReadResultInterest();
    final AccessSafely interestAccess = interest.afterCompleting(1);

    stateStore.read("1", VeterinarianData.class, interest);

    final VeterinarianData vet = interestAccess.readFrom("item", "1");
    assertEquals("1", vet.id);
    assertEquals("112233", vet.contactInformation.telephone.number);
    assertEquals("London", vet.contactInformation.postalAddress.city);
    assertEquals("St.", vet.contactInformation.postalAddress.streetAddress);
    assertEquals("UK", vet.contactInformation.postalAddress.stateProvince);
  }

  @Test
  public void chooseSpecialty() {
    registerExampleVets();

    final CountingProjectionControl control = new CountingProjectionControl();
    final AccessSafely access = control.afterCompleting(1);

    final VeterinarianSpecialtyChosen eventData =
        new VeterinarianSpecialtyChosen(new VeterinarianState("1", null, null, Specialty.from("Dentistry")));
    BaseEntry.TextEntry textEntry = new BaseEntry.TextEntry(VeterinarianSpecialtyChosen.class, 1,
        JsonSerialization.serialized(eventData), 2, Metadata.withObject(eventData));
    final String projectionId = UUID.randomUUID().toString();
    valueToProjectionId.put("1", projectionId);
    Projectable p = new TextProjectable(null, Collections.singletonList(textEntry), projectionId);

    projection.projectWith(p, control);

    final Map<String, Integer> confirmations = access.readFrom("confirmations");

    assertEquals(1, confirmations.size());
    assertEquals(1, valueOfProjectionIdFor("1", confirmations));

    final CountingReadResultInterest interest = new CountingReadResultInterest();
    final AccessSafely interestAccess = interest.afterCompleting(1);

    stateStore.read("1", VeterinarianData.class, interest);

    final VeterinarianData vet = interestAccess.readFrom("item", "1");
    assertEquals("1", vet.id);
    assertEquals("Dentistry", vet.specialty.specialtyTypeId);
  }

  private int valueOfProjectionIdFor(final String valueText, final Map<String, Integer> confirmations) {
    return confirmations.get(valueToProjectionId.get(valueText));
  }
}
