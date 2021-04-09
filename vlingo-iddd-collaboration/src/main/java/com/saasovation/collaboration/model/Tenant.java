// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.saasovation.collaboration.model;

public final class Tenant extends Id {
  public static Tenant fromExisting(final String referencedId) {
    return new Tenant(referencedId);
  }

  public static Tenant unique() {
    return new Tenant();
  }

  private Tenant() {
    super();
  }

  private Tenant(final String referencedId) {
    super(referencedId);
  }
}
