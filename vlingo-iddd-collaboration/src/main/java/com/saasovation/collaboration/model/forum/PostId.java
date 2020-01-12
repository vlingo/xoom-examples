// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.saasovation.collaboration.model.forum;

import com.saasovation.collaboration.model.Id;

public class PostId extends Id {
  public static PostId fromExisting(final String referencedId) {
    return new PostId(referencedId);
  }

  public static PostId unique() {
    return new PostId();
  }

  private PostId() {
    super();
  }

  private PostId(final String referencedId) {
    super(referencedId);
  }
}
