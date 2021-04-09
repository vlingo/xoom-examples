// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.entity.sourced;

import io.vlingo.common.Completes;
import io.vlingo.entity.Events.OrganizationDefined;
import io.vlingo.entity.Events.OrganizationDescribed;
import io.vlingo.entity.Events.OrganizationDisabled;
import io.vlingo.entity.Events.OrganizationEnabled;
import io.vlingo.entity.Events.OrganizationRenamed;
import io.vlingo.entity.Id;
import io.vlingo.entity.Organization;
import io.vlingo.entity.OrganizationState;
import io.vlingo.lattice.model.sourcing.EventSourced;

public class OrganizationEntity extends EventSourced implements Organization {
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
    return apply(new OrganizationDefined(this.state.organizationId, name, description), () -> state);
  }

  @Override
  public Completes<OrganizationState> enable() {
    return apply(new OrganizationEnabled(this.state.organizationId), () -> state);
  }

  @Override
  public Completes<OrganizationState> disable() {
    return apply(new OrganizationDisabled(this.state.organizationId), () -> state);
  }

  @Override
  public Completes<OrganizationState> describeAs(final String description) {
    return apply(new OrganizationDescribed(state.organizationId, description), () -> state);
  }

  @Override
  public Completes<OrganizationState> renameTo(final String name) {
    return apply(new OrganizationRenamed(state.organizationId, name), () -> state);
  }

  static {
    EventSourced.registerConsumer(OrganizationEntity.class, OrganizationDefined.class, OrganizationEntity::applyOrganizationDefined);
    EventSourced.registerConsumer(OrganizationEntity.class, OrganizationEnabled.class, OrganizationEntity::applyOrganizationEnabled);
    EventSourced.registerConsumer(OrganizationEntity.class, OrganizationDisabled.class, OrganizationEntity::applyOrganizationDisabled);
    EventSourced.registerConsumer(OrganizationEntity.class, OrganizationDescribed.class, OrganizationEntity::applyOrganizationDescribed);
    EventSourced.registerConsumer(OrganizationEntity.class, OrganizationRenamed.class, OrganizationEntity::applyOrganizationRenamed);
  }

  private void applyOrganizationDefined(final OrganizationDefined e) {
    state = state.withName(e.name).withDescription(e.description);
  }

  private void applyOrganizationEnabled(final OrganizationEnabled e) {
    state = state.enable();
  }

  private void applyOrganizationDisabled(final OrganizationDisabled e) {
    state = state.disable();
  }

  private void applyOrganizationDescribed(final OrganizationDescribed e) {
    state = state.withDescription(e.description);
  }

  private void applyOrganizationRenamed(final OrganizationRenamed e) {
    state = state.withName(e.name);
  }
}
