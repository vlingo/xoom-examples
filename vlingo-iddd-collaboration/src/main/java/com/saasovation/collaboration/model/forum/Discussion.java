// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.saasovation.collaboration.model.forum;

import com.saasovation.collaboration.model.Author;

import io.vlingo.common.Completes;

public interface Discussion {
  void close();
  Completes<Post> post(final Author author, final String subject, final String bodyText);
  void reopen();
  void topicTo(final String topic);
}
