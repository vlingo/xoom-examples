// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.entity.object;

import io.vlingo.common.Completes;
import io.vlingo.entity.Events.*;
import io.vlingo.entity.Id;
import io.vlingo.entity.Organization;
import io.vlingo.entity.OrganizationState;
import io.vlingo.lattice.model.object.ObjectEntity;

public class OrganizationEntity extends ObjectEntity<State> implements Organization {
  private State state;

  public OrganizationEntity(final Id organizationId) {
    super(organizationId.value);
    this.state = State.from(organizationId);
  }

  public OrganizationEntity() {
    super(null);
    this.state = null;
  }

  @Override
  public Completes<OrganizationState> defineWith(final String name, final String description) {
    state.setName(name);
    state.setDescription(description);
    return apply(state, new OrganizationDefined(this.state.organizationId, name, description), () -> state);
  }

  @Override
  public Completes<OrganizationState> enable() {
    state.enable();
    return apply(state, new OrganizationEnabled(this.state.organizationId), () -> state);
  }

  @Override
  public Completes<OrganizationState> disable() {
    state.disable();
    return apply(state, new OrganizationDisabled(this.state.organizationId), () -> state);
  }

  @Override
  public Completes<OrganizationState> describeAs(final String description) {
    state.setDescription(description);
    return apply(state, new OrganizationDescribed(state.organizationId, description), () -> state);
  }

  @Override
  public Completes<OrganizationState> renameTo(final String name) {
    state.setName(name);
    return apply(state, new OrganizationRenamed(state.organizationId, name), () -> state);
  }

  protected String id() {
    return String.valueOf(state.persistenceId());
  }

  @Override
  protected State stateObject() {
    return state;
  }

  @Override
  protected void stateObject(final State stateObject) {
    this.state = stateObject;
  }

  @Override
  protected Class<State> stateObjectType() {
    return State.class;
  }

}
