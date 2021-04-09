// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.saasovation.collaboration.model;

public final class Moderator extends Id {
  public static Moderator fromExisting(final String referencedId) {
    return new Moderator(referencedId);
  }

  public static Moderator unique() {
    return new Moderator();
  }

  private Moderator() {
    super();
  }

  private Moderator(final String referencedId) {
    super(referencedId);
  }
}
