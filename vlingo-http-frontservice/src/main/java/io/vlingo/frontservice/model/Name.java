// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.frontservice.model;

public final class Name {
  public final String given;
  public final String family;
  
  public static Name from(final String given, final String family) {
    return new Name(given, family);
  }
  
  public Name(final String given, final String family) {
    this.given = given;
    this.family = family;
  }

  @Override
  public String toString() {
    return "Name[given=" + given + ", family=" + family + "]";
  }
}
