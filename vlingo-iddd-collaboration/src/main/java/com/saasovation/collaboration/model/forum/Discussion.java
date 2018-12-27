// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.saasovation.collaboration.model.forum;

import com.saasovation.collaboration.model.Author;

import io.vlingo.common.Completes;
import io.vlingo.common.Tuple2;

public interface Discussion {
  void close();
  Completes<Tuple2<PostId,Post>> postFor(final Author author, final String subject, final String bodyText);
  void reopen();
  void startWith(final Author author, final String topic);
  void topicTo(final String topic);
}
