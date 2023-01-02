// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.saasovation.collaboration.model;

public final class Author extends Id {
  public static Author fromExisting(final String referencedId) {
    return new Author(referencedId);
  }

  public static Author unique() {
    return new Author();
  }

  private Author() {
    super();
  }

  private Author(final String referencedId) {
    super(referencedId);
  }
}
