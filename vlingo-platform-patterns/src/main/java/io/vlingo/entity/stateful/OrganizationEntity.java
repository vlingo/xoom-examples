// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.entity.stateful;

import io.vlingo.common.Completes;
import io.vlingo.entity.Events.OrganizationDefined;
import io.vlingo.entity.Events.OrganizationDescribed;
import io.vlingo.entity.Events.OrganizationEnabled;
import io.vlingo.entity.Events.OrganizationRenamed;
import io.vlingo.entity.Id;
import io.vlingo.entity.Organization;
import io.vlingo.entity.OrganizationState;
import io.vlingo.lattice.model.stateful.StatefulEntity;

public class OrganizationEntity extends StatefulEntity<State> implements Organization {
  private State state;

  public OrganizationEntity(final Id organizationId) {
    super(organizationId.value);
    this.state = State.from(organizationId);
  }

  @Override
  public Completes<OrganizationState> defineWith(final String name, final String description) {
    return apply(state.withName(name).withDescription(description), new OrganizationDefined(this.state.organizationId, name, description), () -> state);
  }

  @Override
  public Completes<OrganizationState> enable() {
    return apply(state.enable(), new OrganizationEnabled(this.state.organizationId), () -> state);
  }

  @Override
  public Completes<OrganizationState> disable() {
    return apply(state.disable(), new OrganizationEnabled(this.state.organizationId), () -> state);
  }

  @Override
  public Completes<OrganizationState> describeAs(final String description) {
    return apply(state.withDescription(description), new OrganizationDescribed(state.organizationId, description), () -> state);
  }

  @Override
  public Completes<OrganizationState> renameTo(final String name) {
    return apply(state.withName(name), new OrganizationRenamed(state.organizationId, name), () -> state);
  }

  @Override
  protected void state(final State state) {
    this.state = state;
  }

  @Override
  protected Class<State> stateType() {
    return State.class;
  }
}
