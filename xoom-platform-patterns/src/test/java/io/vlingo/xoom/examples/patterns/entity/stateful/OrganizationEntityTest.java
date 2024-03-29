// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.patterns.entity.stateful;

import java.util.Arrays;

import io.vlingo.xoom.cluster.StaticClusterConfiguration;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.vlingo.xoom.actors.Address;
import io.vlingo.xoom.actors.Configuration;
import io.vlingo.xoom.lattice.grid.Grid;
import io.vlingo.xoom.examples.patterns.entity.ClusterProperties;
import io.vlingo.xoom.examples.patterns.entity.MockDispatcher;
import io.vlingo.xoom.examples.patterns.entity.Organization;
import io.vlingo.xoom.examples.patterns.entity.OrganizationState;
import io.vlingo.xoom.lattice.model.stateful.StatefulTypeRegistry;
import io.vlingo.xoom.lattice.model.stateful.StatefulTypeRegistry.Info;
import io.vlingo.xoom.symbio.store.state.StateStore;
import io.vlingo.xoom.symbio.store.state.inmemory.InMemoryStateStoreActor;

public class OrganizationEntityTest {
  private static String DefaultDescription = "Object description.";
  private static String DefaultName = "Object";

  private Grid grid;
  private StatefulTypeRegistry registry;
  private StateStore stateStore;

  @Test
  public void testThatOrganizationIsDefined() {
    final OrganizationState state = Organization.statefulWith(grid, DefaultName, DefaultDescription).await();
    Assert.assertEquals(DefaultName, state.name());
    Assert.assertEquals(DefaultDescription, state.description());
  }

  @Test
  public void testThatOrganizationIsRenamed() {
    final OrganizationState state =
            Organization.statefulWith(grid, DefaultName, DefaultDescription)
              .andThenTo(s -> grid.actorOf(Organization.class, address(s.id())))
              .andThenTo(organization -> organization.renameTo(DefaultName + 1))
              .await();

    Assert.assertEquals(DefaultName + 1, state.name());
  }

  @Test
  public void testThatOrganizationIsDescribed() {
    final OrganizationState state =
            Organization.statefulWith(grid, DefaultName, DefaultDescription)
              .andThenTo(s -> grid.actorOf(Organization.class, address(s.id())))
              .andThenTo(organization -> organization.describeAs((DefaultDescription + 1)))
              .await();

    Assert.assertEquals(DefaultDescription + 1, state.description());
  }

  @Before
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public void setUp() throws Exception {
    final StaticClusterConfiguration staticConfiguration = StaticClusterConfiguration.oneNode();
    grid = Grid.start("stateful-entity-test", Configuration.define(), staticConfiguration.properties, staticConfiguration.propertiesOf(0));
    grid.quorumAchieved();
    stateStore = grid.actorFor(StateStore.class, InMemoryStateStoreActor.class, Arrays.asList(new MockDispatcher()));
    registry = new StatefulTypeRegistry(grid.world());

    final Info<Organization> info = new Info(stateStore, io.vlingo.xoom.examples.patterns.entity.stateful.State.class, "StateStore");

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
