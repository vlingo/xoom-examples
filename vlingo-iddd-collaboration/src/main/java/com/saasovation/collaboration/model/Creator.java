// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.saasovation.collaboration.model;

public final class Creator extends Id {
  public static Creator fromExisting(final String referencedId) {
    return new Creator(referencedId);
  }

  public static Creator unique() {
    return new Creator();
  }

  private Creator() {
    super();
  }

  private Creator(final String referencedId) {
    super(referencedId);
  }
}
