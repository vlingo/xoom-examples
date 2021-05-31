package io.vlingo.xoom.examples.petclinic;

import io.vlingo.xoom.actors.World;
import io.vlingo.xoom.examples.petclinic.infrastructure.*;
import io.vlingo.xoom.examples.petclinic.infrastructure.persistence.VeterinarianQueries;
import io.vlingo.xoom.examples.petclinic.infrastructure.persistence.VeterinarianQueriesActor;
import io.vlingo.xoom.lattice.model.stateful.StatefulTypeRegistry;
import io.vlingo.xoom.symbio.store.dispatch.NoOpDispatcher;
import io.vlingo.xoom.symbio.store.state.StateStore;
import io.vlingo.xoom.symbio.store.state.inmemory.InMemoryStateStoreActor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VeterinarianQueryTests {

  private static final StateStore.WriteResultInterest NOOP_WRI = new NoopWriteResultInterest();

  private World world;
  private StateStore stateStore;
  private VeterinarianQueries queries;

  @BeforeEach
  public void setUp() {
    world = World.startWithDefaults("test-state-store-query");
    stateStore = world.actorFor(StateStore.class, InMemoryStateStoreActor.class,
        Collections.singletonList(new NoOpDispatcher()));
    StatefulTypeRegistry.registerAll(world, stateStore, VeterinarianData.class);
    queries = world.actorFor(VeterinarianQueries.class, VeterinarianQueriesActor.class, stateStore);
  }

  @Test
  public void vetOfEmptyResult() {
    VeterinarianData item = queries.veterinarianOf("1").await();
    assertEquals("", item.id);
  }

  @Test
  public void vetOf() {
    ContactInformationData contact = ContactInformationData.from(
        PostalAddressData.from("St.", "City", "UK", "123"),
        TelephoneData.from("99110011")
    );
    stateStore.write("1", VeterinarianData.from("1", FullNameData.from("Harry", "Potter"),
        contact, SpecialtyData.from("Behaviour")), 1, NOOP_WRI);
    stateStore.write("2", VeterinarianData.from("2", FullNameData.from("Draco", "Malfoy"),
        contact, SpecialtyData.from("Surgery")), 1, NOOP_WRI);
    VeterinarianData item = queries.veterinarianOf("1").await();
    assertEquals("1", item.id);
    assertEquals("Harry", item.name.first);
    assertEquals("Potter", item.name.last);
    assertEquals("123", item.contactInformation.postalAddress.postalCode);
    assertEquals("UK", item.contactInformation.postalAddress.stateProvince);
    assertEquals("St.", item.contactInformation.postalAddress.streetAddress);
    assertEquals("99110011", item.contactInformation.telephone.number);
    assertEquals("Behaviour", item.specialty.specialtyTypeId);

    item = queries.veterinarianOf("2").await();
    assertEquals("2", item.id);
    assertEquals("Draco", item.name.first);
    assertEquals("Malfoy", item.name.last);
  }

  @Test
  public void vets() {
    ContactInformationData contact = ContactInformationData.from(
        PostalAddressData.from("St.", "City", "UK", "123"),
        TelephoneData.from("99110011")
    );
    stateStore.write("1", VeterinarianData.from("1", FullNameData.from("Harry", "Potter"),
        contact, SpecialtyData.from("Behaviour")), 1, NOOP_WRI);
    stateStore.write("2", VeterinarianData.from("2", FullNameData.from("Draco", "Malfoy"),
        contact, SpecialtyData.from("Surgery")), 1, NOOP_WRI);
    Collection<VeterinarianData> animalTypes = queries.veterinarians().await();
    assertEquals(2, animalTypes.size());
  }

  @Test
  public void vets2() {
    ContactInformationData contact = ContactInformationData.from(
        PostalAddressData.from("St.", "City", "UK", "123"),
        TelephoneData.from("99110011")
    );
    stateStore.write("1", VeterinarianData.from("1", FullNameData.from("Harry", "Potter"),
        contact, SpecialtyData.from("Behaviour")), 1, NOOP_WRI);
    Collection<VeterinarianData> animalTypes = queries.veterinarians().await();
    assertEquals(1, animalTypes.size());
    VeterinarianData item = animalTypes.stream().findFirst().orElseThrow(RuntimeException::new);
    assertEquals("1", item.id);
    assertEquals("Harry", item.name.first);
    assertEquals("Potter", item.name.last);
    assertEquals("123", item.contactInformation.postalAddress.postalCode);
    assertEquals("UK", item.contactInformation.postalAddress.stateProvince);
    assertEquals("St.", item.contactInformation.postalAddress.streetAddress);
    assertEquals("99110011", item.contactInformation.telephone.number);
    assertEquals("Behaviour", item.specialty.specialtyTypeId);
  }
}
