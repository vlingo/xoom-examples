package io.vlingo.developers.petclinic;

import io.vlingo.actors.World;
import io.vlingo.developers.petclinic.infrastructure.ContactInformationData;
import io.vlingo.developers.petclinic.infrastructure.FullnameData;
import io.vlingo.developers.petclinic.infrastructure.SpecialtyData;
import io.vlingo.developers.petclinic.infrastructure.VeterinarianData;
import io.vlingo.developers.petclinic.infrastructure.persistence.VeterinarianQueries;
import io.vlingo.developers.petclinic.infrastructure.persistence.VeterinarianQueriesActor;
import io.vlingo.developers.petclinic.model.PostalAddress;
import io.vlingo.developers.petclinic.model.Telephone;
import io.vlingo.lattice.model.stateful.StatefulTypeRegistry;
import io.vlingo.symbio.store.dispatch.NoOpDispatcher;
import io.vlingo.symbio.store.state.StateStore;
import io.vlingo.symbio.store.state.inmemory.InMemoryStateStoreActor;
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
    public void setUp(){
        world = World.startWithDefaults("test-state-store-query");
        stateStore = world.actorFor(StateStore.class, InMemoryStateStoreActor.class,
                                    Collections.singletonList(new NoOpDispatcher()));
        StatefulTypeRegistry.registerAll(world, stateStore, VeterinarianData.class);
        queries = world.actorFor(VeterinarianQueries.class, VeterinarianQueriesActor.class, stateStore);
    }

    @Test
    public void vetOfEmptyResult(){
        VeterinarianData item = queries.veterinarianOf("1").await();
        assertEquals("", item.id);
    }

    @Test
    public void vetOf(){
        ContactInformationData contact = ContactInformationData.of(
                PostalAddress.of("St.", "City", "UK", "123"),
                Telephone.of("99110011")
        );
        stateStore.write("1", VeterinarianData.from("1", FullnameData.of("Harry", "Potter"),
                            contact, SpecialtyData.of("Behaviour")), 1, NOOP_WRI);
        stateStore.write("2", VeterinarianData.from("2", FullnameData.of("Draco", "Malfoy"),
                            contact, SpecialtyData.of("Surgery")), 1, NOOP_WRI);
        VeterinarianData item = queries.veterinarianOf("1").await();
        assertEquals("1", item.id);
        assertEquals("Harry", item.name.first);
        assertEquals("Potter", item.name.last);
        assertEquals("123", item.contact.postalAddress.postalCode);
        assertEquals("UK", item.contact.postalAddress.stateProvince);
        assertEquals("St.", item.contact.postalAddress.streetAddress);
        assertEquals("99110011", item.contact.telephone.number);
        assertEquals("Behaviour", item.specialty.specialtyTypeId);

        item = queries.veterinarianOf("2").await();
        assertEquals("2", item.id);
        assertEquals("Draco", item.name.first);
        assertEquals("Malfoy", item.name.last);
    }

    @Test
    public void vets(){
        ContactInformationData contact = ContactInformationData.of(
                PostalAddress.of("St.", "City", "UK", "123"),
                Telephone.of("99110011")
        );
        stateStore.write("1", VeterinarianData.from("1", FullnameData.of("Harry", "Potter"),
                            contact, SpecialtyData.of("Behaviour")), 1, NOOP_WRI);
        stateStore.write("2", VeterinarianData.from("2", FullnameData.of("Draco", "Malfoy"),
                            contact, SpecialtyData.of("Surgery")), 1, NOOP_WRI);
        Collection<VeterinarianData> animalTypes = queries.veterinarians().await();
        assertEquals(2, animalTypes.size());
    }

    @Test
    public void vets2(){
        ContactInformationData contact = ContactInformationData.of(
                PostalAddress.of("St.", "City", "UK", "123"),
                Telephone.of("99110011")
        );
        stateStore.write("1", VeterinarianData.from("1", FullnameData.of("Harry", "Potter"),
                            contact, SpecialtyData.of("Behaviour")), 1, NOOP_WRI);
        Collection<VeterinarianData> animalTypes = queries.veterinarians().await();
        assertEquals(1, animalTypes.size());
        VeterinarianData item = animalTypes.stream().findFirst().orElseThrow(RuntimeException::new);
        assertEquals("1", item.id);
        assertEquals("Harry", item.name.first);
        assertEquals("Potter", item.name.last);
        assertEquals("123", item.contact.postalAddress.postalCode);
        assertEquals("UK", item.contact.postalAddress.stateProvince);
        assertEquals("St.", item.contact.postalAddress.streetAddress);
        assertEquals("99110011", item.contact.telephone.number);
        assertEquals("Behaviour", item.specialty.specialtyTypeId);
    }
}
