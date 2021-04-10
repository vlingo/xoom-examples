// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.patterns.entity;

import io.vlingo.xoom.actors.Actor;
import io.vlingo.xoom.actors.Address;
import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.common.Completes;

public interface Organization {
  static String nameFrom(final Id organizationId) {
    return "O:"+organizationId.value;
  }
  
  static String nameFrom(final String organizationId) {
    return "O:"+organizationId;
  }

  static Completes<OrganizationState> objectWith(
          final Stage stage,
          final String name,
          final String description) {
    return with(stage, io.vlingo.xoom.examples.patterns.entity.object.OrganizationEntity.class, Id.unique(), name, description);
  }

  static Completes<OrganizationState> sourcedWith(
          final Stage stage,
          final String name,
          final String description) {
    return with(stage, io.vlingo.xoom.examples.patterns.entity.sourced.OrganizationEntity.class, Id.unique(), name, description);
  }

  static Completes<OrganizationState> statefulWith(
          final Stage stage,
          final String name,
          final String description) {
    return with(stage, io.vlingo.xoom.examples.patterns.entity.stateful.OrganizationEntity.class, Id.unique(), name, description);
  }

  static Completes<OrganizationState> with(
          final Stage stage,
          final Class<? extends Actor> entityType,
          final Id organizationId,
          final String name,
          final String description) {
    final String actorName = nameFrom(organizationId);
    final Address address = stage.addressFactory().from(organizationId.value, actorName);
    final Definition definition = Definition.has(entityType, Definition.parameters(organizationId), actorName);
    final Organization organization = stage.actorFor(Organization.class, definition, address);
    return organization.defineWith(name, description);
  }

  Completes<OrganizationState> defineWith(final String name, final String description);

  Completes<OrganizationState> describeAs(final String description);

  Completes<OrganizationState> renameTo(final String name);
  
  Completes<OrganizationState> enable();
  
  Completes<OrganizationState> disable();
}
