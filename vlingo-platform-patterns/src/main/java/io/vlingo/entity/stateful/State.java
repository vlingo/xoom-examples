// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.entity.stateful;

import io.vlingo.entity.Id;
import io.vlingo.entity.OrganizationState;

public class State implements OrganizationState {

  public final Id organizationId;
  private String name;
  private String description;
  private final boolean enabled;

  public static State from(final Id organizationId) {
    return new State(organizationId);
  }

  @Override
  public String id() {
    return organizationId.value;
  }

  @Override
  public boolean enabled() {
    return enabled;
  }

  public State enable() {
    return new State(organizationId, name, description, true);
  }

  public State disable() {
    return new State(organizationId, name, description, false);
  }

  @Override
  public String description() {
    return description;
  }

  @Override
  public String name() {
    return name;
  }

  State withDescription(final String description) {
    return new State(organizationId, name, description, enabled);
  }

  State withName(final String name) {
    return new State(organizationId, name, description, enabled);
  }

  private State(final Id organizationId) {
    this(organizationId, "", "", false);
  }

  private State(final Id organizationId, final String name, final String description, final boolean enabled) {
    this.organizationId = organizationId;
    this.name = name;
    this.description = description;
    this.enabled = enabled;
  }
}
