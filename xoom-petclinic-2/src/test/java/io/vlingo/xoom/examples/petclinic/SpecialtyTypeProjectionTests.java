package io.vlingo.xoom.examples.petclinic;

import io.vlingo.xoom.actors.World;
import io.vlingo.xoom.actors.testkit.AccessSafely;
import io.vlingo.xoom.common.serialization.JsonSerialization;
import io.vlingo.xoom.examples.petclinic.infrastructure.SpecialtyTypeData;
import io.vlingo.xoom.examples.petclinic.infrastructure.persistence.QueryModelStateStoreProvider;
import io.vlingo.xoom.examples.petclinic.infrastructure.persistence.SpecialtyTypeProjectionActor;
import io.vlingo.xoom.examples.petclinic.model.specialtytype.SpecialtyTypeOffered;
import io.vlingo.xoom.examples.petclinic.model.specialtytype.SpecialtyTypeRenamed;
import io.vlingo.xoom.examples.petclinic.model.specialtytype.SpecialtyTypeState;
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

public class SpecialtyTypeProjectionTests {

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
    StatefulTypeRegistry statefulTypeRegistry = StatefulTypeRegistry.registerAll(world, stateStore, SpecialtyTypeData.class);
    QueryModelStateStoreProvider.using(world.stage(), statefulTypeRegistry);
    projection = world.actorFor(Projection.class, SpecialtyTypeProjectionActor.class, stateStore);
  }

  private Projectable createSpecialtyTypeOffered(String id, String name) {
    final SpecialtyTypeOffered eventData = new SpecialtyTypeOffered(new SpecialtyTypeState(id, name));

    BaseEntry.TextEntry textEntry = new BaseEntry.TextEntry(SpecialtyTypeOffered.class, 1,
        JsonSerialization.serialized(eventData), 1, Metadata.withObject(eventData));

    final String projectionId = UUID.randomUUID().toString();
    valueToProjectionId.put(id, projectionId);
    return new TextProjectable(null, Collections.singletonList(textEntry), projectionId);
  }

  @Test
  public void register() {
    final CountingProjectionControl control = new CountingProjectionControl();

    final AccessSafely access = control.afterCompleting(2);

    projection.projectWith(createSpecialtyTypeOffered("1", "Behaviour"), control);
    projection.projectWith(createSpecialtyTypeOffered("2", "Dentistry"), control);

    final Map<String, Integer> confirmations = access.readFrom("confirmations");

    assertEquals(2, confirmations.size());
    assertEquals(1, valueOfProjectionIdFor("1", confirmations));
    assertEquals(1, valueOfProjectionIdFor("2", confirmations));

    CountingReadResultInterest interest = new CountingReadResultInterest();
    AccessSafely interestAccess = interest.afterCompleting(1);
    stateStore.read("1", SpecialtyTypeData.class, interest);
    SpecialtyTypeData item = interestAccess.readFrom("item", "1");
    assertEquals("1", item.id);
    assertEquals("Behaviour", item.name);

    interest = new CountingReadResultInterest();
    interestAccess = interest.afterCompleting(1);
    stateStore.read("2", SpecialtyTypeData.class, interest);
    item = interestAccess.readFrom("item", "2");
    assertEquals("2", item.id);
    assertEquals("Dentistry", item.name);
  }

  private void registerExampleSpecialtyType() {
    final CountingProjectionControl control = new CountingProjectionControl();
    final AccessSafely access = control.afterCompleting(2);
    projection.projectWith(createSpecialtyTypeOffered("1", "Behaviour"), control);
    projection.projectWith(createSpecialtyTypeOffered("2", "Dentistry"), control);
  }

  @Test
  public void changeName() {
    registerExampleSpecialtyType();

    final CountingProjectionControl control = new CountingProjectionControl();
    final AccessSafely access = control.afterCompleting(1);

    final SpecialtyTypeRenamed eventData = new SpecialtyTypeRenamed(new SpecialtyTypeState("1", "Surgery"));
    BaseEntry.TextEntry textEntry = new BaseEntry.TextEntry(SpecialtyTypeRenamed.class, 1,
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

    stateStore.read("1", SpecialtyTypeData.class, interest);

    final SpecialtyTypeData pet = interestAccess.readFrom("item", "1");
    assertEquals("1", pet.id);
    assertEquals("Surgery", pet.name);
  }

  private int valueOfProjectionIdFor(final String valueText, final Map<String, Integer> confirmations) {
    return confirmations.get(valueToProjectionId.get(valueText));
  }
}
