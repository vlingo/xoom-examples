// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.entity.sourced;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.vlingo.actors.Address;
import io.vlingo.actors.Configuration;
import io.vlingo.actors.Grid;
import io.vlingo.entity.ClusterProperties;
import io.vlingo.entity.MockDispatcher;
import io.vlingo.entity.Organization;
import io.vlingo.entity.OrganizationState;
import io.vlingo.lattice.model.sourcing.SourcedTypeRegistry;
import io.vlingo.lattice.model.sourcing.SourcedTypeRegistry.Info;
import io.vlingo.symbio.store.journal.Journal;
import io.vlingo.symbio.store.journal.inmemory.InMemoryJournalActor;

import java.util.Arrays;

public class OrganizationEntityTest {
  private static String DefaultDescription = "Object description.";
  private static String DefaultName = "Object";

  private Grid grid;
  private SourcedTypeRegistry registry;
  private Journal<String> journal;

  @Test
  public void testThatOrganizationIsDefined() {
    final OrganizationState state = Organization.sourcedWith(grid, DefaultName, DefaultDescription).await();
    Assert.assertEquals(DefaultName, state.name());
    Assert.assertEquals(DefaultDescription, state.description());
  }

  @Test
  public void testThatOrganizationIsRenamed() {
    final OrganizationState state =
            Organization.sourcedWith(grid, DefaultName, DefaultDescription)
              .andThenTo(s -> grid.actorOf(Organization.class, address(s.id())))
              .andThenTo(organization -> organization.renameTo(DefaultName + 1))
              .await();

    Assert.assertEquals(DefaultName + 1, state.name());
  }

  @Test
  public void testThatOrganizationIsDescribed() {
    final OrganizationState state =
            Organization.sourcedWith(grid, DefaultName, DefaultDescription)
              .andThenTo(s -> grid.actorOf(Organization.class, address(s.id())))
              .andThenTo(organization -> organization.describeAs((DefaultDescription + 1)))
              .await();

    Assert.assertEquals(DefaultDescription + 1, state.description());
  }

  @Before
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public void setUp() throws Exception {
    grid = Grid.start("sourced-entity-test", Configuration.define(), ClusterProperties.oneNode(), "node1");
    grid.quorumAchieved();
    journal = grid.actorFor(Journal.class, InMemoryJournalActor.class, Arrays.asList(new MockDispatcher()));
    registry = new SourcedTypeRegistry(grid.world());

    final Info<Organization> info = new Info(journal, io.vlingo.entity.sourced.OrganizationEntity.class, "Journal");

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
