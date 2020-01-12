// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.saasovation.collaboration.model.forum;

import com.saasovation.collaboration.model.Id;

public class ForumId extends Id {
  public static ForumId fromExisting(final String referencedId) {
    return new ForumId(referencedId);
  }

  public static ForumId unique() {
    return new ForumId();
  }

  private ForumId() {
    super();
  }

  private ForumId(final String referencedId) {
    super(referencedId);
  }
}
