// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.patterns.entity;

import io.vlingo.xoom.lattice.model.DomainEvent;

public final class Events {

  public static final class OrganizationDefined extends DomainEvent {
    public final String Id;
    public final String name;
    public final String description;

    public static OrganizationDefined with(final Id Id, final String name, final String description) {
      return new OrganizationDefined(Id, name, description);
    }

    public OrganizationDefined(final Id Id, final String name, final String description) {
      this.Id = Id.value;
      this.name = name;
      this.description = description;
    }
  }

  public static final class OrganizationDescribed extends DomainEvent {
    public final String Id;
    public final String description;

    public static OrganizationDescribed with(final Id Id, final String description) {
      return new OrganizationDescribed(Id, description);
    }

    public OrganizationDescribed(final Id Id, final String description) {
      this.Id = Id.value;
      this.description = description;
    }
  }

  public static final class OrganizationEnabled extends DomainEvent {
    public final String id;

    public static OrganizationEnabled with(final Id id) {
      return new OrganizationEnabled(id);
    }

    public OrganizationEnabled(final Id id) {
      this.id = id.value;
    }
  }

  public static final class OrganizationDisabled extends DomainEvent {
    public final String id;

    public static OrganizationDisabled with(final Id id) {
      return new OrganizationDisabled(id);
    }

    public OrganizationDisabled(final Id id) {
      this.id = id.value;
    }
  }

  public static final class OrganizationRenamed extends DomainEvent {
    public final String Id;
    public final String name;

    public static OrganizationRenamed with(final Id Id, final String name) {
      return new OrganizationRenamed(Id, name);
    }

    public OrganizationRenamed(final Id Id, final String name) {
      this.Id = Id.value;
      this.name = name;
    }
  }
}
