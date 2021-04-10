package io.vlingo.xoom.examples.petclinic;

import io.vlingo.xoom.actors.World;
import io.vlingo.xoom.actors.testkit.AccessSafely;
import io.vlingo.xoom.common.serialization.JsonSerialization;
import io.vlingo.xoom.examples.petclinic.infrastructure.ClientData;
import io.vlingo.xoom.examples.petclinic.infrastructure.persistence.ClientProjectionActor;
import io.vlingo.xoom.examples.petclinic.infrastructure.persistence.QueryModelStateStoreProvider;
import io.vlingo.xoom.examples.petclinic.model.ContactInformation;
import io.vlingo.xoom.examples.petclinic.model.Fullname;
import io.vlingo.xoom.examples.petclinic.model.PostalAddress;
import io.vlingo.xoom.examples.petclinic.model.Telephone;
import io.vlingo.xoom.examples.petclinic.model.client.ClientContactInformationChanged;
import io.vlingo.xoom.examples.petclinic.model.client.ClientNameChanged;
import io.vlingo.xoom.examples.petclinic.model.client.ClientRegistered;
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

public class ClientProjectionTests {

    private World world;
    private StateStore stateStore;
    private Projection projection;
    private Map<String, String> valueToProjectionId;

    @BeforeEach
    public void setUp(){
        world = World.startWithDefaults("test-state-store-projection");
        NoOpDispatcher dispatcher = new NoOpDispatcher();
        valueToProjectionId = new ConcurrentHashMap<>();
        stateStore = world.actorFor(StateStore.class, InMemoryStateStoreActor.class, Collections.singletonList(dispatcher));
        StatefulTypeRegistry statefulTypeRegistry = StatefulTypeRegistry.registerAll(world, stateStore, ClientData.class);
        QueryModelStateStoreProvider.using(world.stage(), statefulTypeRegistry);
        projection = world.actorFor(Projection.class, ClientProjectionActor.class, stateStore);
    }

    private Projectable createClientRegistered(String id, Fullname name, ContactInformation contact){
        final ClientRegistered eventData = new ClientRegistered(id, name, contact);

        BaseEntry.TextEntry textEntry = new BaseEntry.TextEntry(ClientRegistered.class, 1,
                JsonSerialization.serialized(eventData), 1, Metadata.withObject(eventData));

        final String projectionId = UUID.randomUUID().toString();
        valueToProjectionId.put(id, projectionId);
        return new TextProjectable(null, Collections.singletonList(textEntry), projectionId);
    }

    @Test
    public void register(){
        final CountingProjectionControl control = new CountingProjectionControl();

        final AccessSafely access = control.afterCompleting(2);

        projection.projectWith(createClientRegistered(
                "1",
                Fullname.of("Harry", "Potter"),
                ContactInformation.of(
                        PostalAddress.of("St.", "City", "UK", "123"),
                        Telephone.of("99110011")
                )), control);
        projection.projectWith(createClientRegistered(
                "2",
                Fullname.of("Draco", "Malfoy"),
                ContactInformation.of(
                        PostalAddress.of("St.", "City", "UK", "123"),
                        Telephone.of("88110011")
                )), control);

        final Map<String,Integer> confirmations = access.readFrom("confirmations");

        assertEquals(2, confirmations.size());
        assertEquals(1, valueOfProjectionIdFor("1", confirmations));
        assertEquals(1, valueOfProjectionIdFor("2", confirmations));

        CountingReadResultInterest interest = new CountingReadResultInterest();
        AccessSafely interestAccess = interest.afterCompleting(1);
        stateStore.read("1", ClientData.class, interest);
        ClientData item = interestAccess.readFrom("item", "1");
        assertEquals("1", item.id);
        assertEquals("Harry", item.name.first);
        assertEquals("Potter", item.name.last);
        assertEquals("123", item.contact.postalAddress.postalCode);
        assertEquals("UK", item.contact.postalAddress.stateProvince);
        assertEquals("St.", item.contact.postalAddress.streetAddress);
        assertEquals("99110011", item.contact.telephone.number);

        interest = new CountingReadResultInterest();
        interestAccess = interest.afterCompleting(1);
        stateStore.read("2", ClientData.class, interest);
        item = interestAccess.readFrom("item", "2");
        assertEquals("2", item.id);
        assertEquals("Draco", item.name.first);
        assertEquals("Malfoy", item.name.last);
    }

    private void registerExampleClient(){
        final CountingProjectionControl control = new CountingProjectionControl();
        final AccessSafely access = control.afterCompleting(2);
        projection.projectWith(createClientRegistered(
                "1",
                Fullname.of("Harry", "Potter"),
                ContactInformation.of(
                        PostalAddress.of("St.", "City", "UK", "123"),
                        Telephone.of("99110011")
                )), control);
        projection.projectWith(createClientRegistered(
                "2",
                Fullname.of("Draco", "Malfoy"),
                ContactInformation.of(
                        PostalAddress.of("St.", "City", "UK", "123"),
                        Telephone.of("88110011")
                )), control);
    }

    @Test
    public void changeName(){
        registerExampleClient();

        final CountingProjectionControl control = new CountingProjectionControl();
        final AccessSafely access = control.afterCompleting(1);

        final ClientNameChanged eventData = new ClientNameChanged("1", Fullname.of("Severus", "Snape"));
        BaseEntry.TextEntry textEntry = new BaseEntry.TextEntry(ClientNameChanged.class, 1,
                JsonSerialization.serialized(eventData), 2, Metadata.withObject(eventData));
        final String projectionId = UUID.randomUUID().toString();
        valueToProjectionId.put("1", projectionId);
        Projectable p = new TextProjectable(null, Collections.singletonList(textEntry), projectionId);

        projection.projectWith(p, control);

        final Map<String,Integer> confirmations = access.readFrom("confirmations");

        assertEquals(1, confirmations.size());
        assertEquals(1, valueOfProjectionIdFor("1", confirmations));

        final CountingReadResultInterest interest = new CountingReadResultInterest();
        final AccessSafely interestAccess = interest.afterCompleting(1);

        stateStore.read("1", ClientData.class, interest);

        final ClientData pet = interestAccess.readFrom("item", "1");
        assertEquals("1", pet.id);
        assertEquals("Severus", pet.name.first);
        assertEquals("Snape", pet.name.last);
    }

    @Test
    public void changeContact(){
        registerExampleClient();

        final CountingProjectionControl control = new CountingProjectionControl();
        final AccessSafely access = control.afterCompleting(1);

        final ClientContactInformationChanged eventData = new ClientContactInformationChanged(
                "1",
                ContactInformation.of(
                        PostalAddress.of("St.", "London", "UK", "123"),
                        Telephone.of("77110011")
                )
        );
        BaseEntry.TextEntry textEntry = new BaseEntry.TextEntry(ClientContactInformationChanged.class, 1,
                JsonSerialization.serialized(eventData), 2, Metadata.withObject(eventData));
        final String projectionId = UUID.randomUUID().toString();
        valueToProjectionId.put("1", projectionId);
        Projectable p = new TextProjectable(null, Collections.singletonList(textEntry), projectionId);

        projection.projectWith(p, control);

        final Map<String,Integer> confirmations = access.readFrom("confirmations");

        assertEquals(1, confirmations.size());
        assertEquals(1, valueOfProjectionIdFor("1", confirmations));

        final CountingReadResultInterest interest = new CountingReadResultInterest();
        final AccessSafely interestAccess = interest.afterCompleting(1);

        stateStore.read("1", ClientData.class, interest);

        final ClientData pet = interestAccess.readFrom("item", "1");
        assertEquals("1", pet.id);
        assertEquals("London", pet.contact.postalAddress.city);
        assertEquals("77110011", pet.contact.telephone.number);
    }

    private int valueOfProjectionIdFor(final String valueText, final Map<String,Integer> confirmations) {
        return confirmations.get(valueToProjectionId.get(valueText));
    }
}
