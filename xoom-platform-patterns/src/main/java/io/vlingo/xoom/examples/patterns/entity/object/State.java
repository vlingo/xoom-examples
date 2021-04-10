// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.patterns.entity.object;

import java.util.Map;

import io.vlingo.xoom.examples.patterns.entity.Id;
import io.vlingo.xoom.examples.patterns.entity.OrganizationState;
import io.vlingo.xoom.symbio.store.object.StateObject;
import io.vlingo.xoom.symbio.store.MapQueryExpression.FluentMap;

public class State extends StateObject implements OrganizationState {
  private static final long serialVersionUID = 1L;

  public final Id organizationId;
  private String name;
  private String description;
  private boolean enabled;

  public static State from(final long id) {
    return State.from(id, 0, null, null, null);
  }

  public static State from(final Id organizationId) {
    return new State(organizationId);
  }

  public static State from(final long id, final long version, final Id organizationId, final String name, final String description) {
    return new State(id, version, organizationId, name, description);
  }

  @Override
  public String id() {
    return organizationId.value;
  }

  @Override
  public String description() {
    return description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  @Override
  public boolean enabled() {
    return enabled;
  }

  public void enable() {
    this.enabled = true;
  }

  public void disable() {
    this.enabled = false;
  }
  
  @Override
  public String name() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  @Override
  public Map<String, Object> queryMap() {
    return FluentMap.has("organizationId", organizationId.value);
  }

  @Override
  public int hashCode() {
    return 31 * this.organizationId.value.hashCode();
  }

  @Override
  public boolean equals(final Object other) {
    if (other == null || other.getClass() != getClass()) {
      return false;
    } else if (this == other) {
      return true;
    }

    final State otherState = (State) other;

    return this.persistenceId() == otherState.persistenceId();
  }

  @Override
  public String toString() {
    return "OrganizationState[persistenceId=" + persistenceId() +
            " version=" + version() +
            " organizationId=" + organizationId.value +
            " name=" + name +
            " description=" + description +
            " enabled=" + enabled + "]";
  }

  private State(final Id organizationId) {
    this(Unidentified, 0, organizationId, "", "");
  }

  private State(final long id, final long version, final Id organizationId, final String name, final String description) {
    super(id, version);
    this.organizationId = organizationId;
    this.name = name;
    this.description = description;
    this.enabled = false;
  }
}
