// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.patterns.entity.sourced;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.vlingo.xoom.actors.Address;
import io.vlingo.xoom.actors.Configuration;
import io.vlingo.xoom.actors.Grid;
import io.vlingo.xoom.examples.patterns.entity.ClusterProperties;
import io.vlingo.xoom.examples.patterns.entity.MockDispatcher;
import io.vlingo.xoom.examples.patterns.entity.Organization;
import io.vlingo.xoom.examples.patterns.entity.OrganizationState;
import io.vlingo.xoom.lattice.model.sourcing.SourcedTypeRegistry;
import io.vlingo.xoom.lattice.model.sourcing.SourcedTypeRegistry.Info;
import io.vlingo.xoom.symbio.store.journal.Journal;
import io.vlingo.xoom.symbio.store.journal.inmemory.InMemoryJournalActor;

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

    final Info<Organization> info = new Info(journal, io.vlingo.xoom.examples.patterns.entity.sourced.OrganizationEntity.class, "Journal");

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
