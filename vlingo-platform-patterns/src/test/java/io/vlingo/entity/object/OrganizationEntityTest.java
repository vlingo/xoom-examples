// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.entity.object;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.vlingo.actors.Address;
import io.vlingo.entity.MockDispatcher;
import io.vlingo.entity.Organization;
import io.vlingo.entity.OrganizationState;
import io.vlingo.lattice.grid.Grid;
import io.vlingo.lattice.grid.GridNodeBootstrap;
import io.vlingo.lattice.model.object.ObjectTypeRegistry;
import io.vlingo.lattice.model.object.ObjectTypeRegistry.Info;
import io.vlingo.symbio.store.object.MapQueryExpression;
import io.vlingo.symbio.store.object.ObjectStore;
import io.vlingo.symbio.store.object.StateObjectMapper;
import io.vlingo.symbio.store.object.inmemory.InMemoryObjectStoreActor;

public class OrganizationEntityTest {
  private static String DefaultDescription = "Object description.";
  private static String DefaultName = "Object";

  private Grid grid;
  private ObjectTypeRegistry registry;
  private ObjectStore objectStore;

  @Test
  public void testThatOrganizationIsDefined() {
    final OrganizationState state = Organization.objectWith(grid, DefaultName, DefaultDescription).await();
    Assert.assertEquals(DefaultName, state.name());
    Assert.assertEquals(DefaultDescription, state.description());
  }

  @Test
  public void testThatOrganizationIsRenamed() {
    final OrganizationState state =
            Organization.objectWith(grid, DefaultName, DefaultDescription)
              .andThenTo(s -> grid.actorOf(Organization.class, address(s.id())))
              .andThenTo(organization -> organization.renameTo(DefaultName + 1))
              .await();

    Assert.assertEquals(DefaultName + 1, state.name());
  }

  @Test
  public void testThatOrganizationIsDescribed() {
    final OrganizationState state =
            Organization.objectWith(grid, DefaultName, DefaultDescription)
              .andThenTo(s -> grid.actorOf(Organization.class, address(s.id())))
              .andThenTo(organization -> organization.describeAs((DefaultDescription + 1)))
              .await();

    Assert.assertEquals(DefaultDescription + 1, state.description());
  }

  @Before
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public void setUp() throws Exception {
    GridNodeBootstrap.reset();
    grid = Grid.startWith("object-entity", "node1");
    objectStore = grid.actorFor(ObjectStore.class, InMemoryObjectStoreActor.class, new MockDispatcher());
    registry = new ObjectTypeRegistry(grid.world());

    final Info<Organization> info =
            new Info(
            objectStore,
            io.vlingo.entity.object.State.class,
            "ObjectStore",
            MapQueryExpression.using(Organization.class, "find", MapQueryExpression.map("id", "id")),
            StateObjectMapper.with(Organization.class, new Object(), new Object()));

    registry.register(info);
  }

  @After
  public void tearDown() {
    grid.terminate();
  }

  private Address address(final String id) {
    return grid.addressFactory().from(id);
  }
}
