// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.saasovation.collaboration.model.forum;

import com.saasovation.collaboration.model.Author;
import com.saasovation.collaboration.model.Moderator;

import io.vlingo.common.Completes;

public interface Forum {
  static ForumId startWith() {
    return null;
  }

  void assign(final Moderator moderator);
  void close();
  void describeAs(final String description);
  Completes<Discussion> discuss(final Author author, final String topic);
  void reopen();
  void topicIs(final String topic);
}
