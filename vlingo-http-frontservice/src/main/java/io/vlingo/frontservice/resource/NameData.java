// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.frontservice.resource;

import io.vlingo.frontservice.model.Name;

public class NameData {
  public final String given;
  public final String family;

  public static NameData from(final String given, final String family) {
    return new NameData(given, family);
  }

  public static NameData from(final Name name) {
    return new NameData(name.given, name.family);
  }
  
  public NameData(final String given, final String family) {
    this.given = given;
    this.family = family;
  }

  @Override
  public String toString() {
    return "NameData[given=" + given + ", family=" + family + "]";
  }
}
