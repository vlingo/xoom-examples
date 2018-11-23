// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.saasovation.collaboration.model.forum;

import com.saasovation.collaboration.model.Id;

public class DiscussionId extends Id {
  public static DiscussionId fromExisting(final String referencedId) {
    return new DiscussionId(referencedId);
  }

  public static DiscussionId unique() {
    return new DiscussionId();
  }

  private DiscussionId() {
    super();
  }

  private DiscussionId(final String referencedId) {
    super(referencedId);
  }
}
