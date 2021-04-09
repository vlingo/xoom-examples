// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.saasovation.collaboration.model.forum;

import com.saasovation.collaboration.model.Author;
import com.saasovation.collaboration.model.Tenant;

import io.vlingo.common.Completes;
import io.vlingo.common.Tuple2;

public interface Discussion {
  void close();
  Completes<Tuple2<PostId,Post>> postFor(final Author author, final String subject, final String bodyText);
  void reopen();
  void startWith(final Author author, final String topic, final String ownerId);
  void topicTo(final String topic);

  public final class State {

    public static State of(String tenantId, String discussionId) {
      return new State(Tenant.fromExisting(tenantId), null, DiscussionId.fromExisting(discussionId));
    }

    public final Tenant tenant;
    public final ForumId forumId;
    public final DiscussionId discussionId;
    public final Author author;
    public final String topic;
    public final boolean open;

    State(final Tenant tenant, final ForumId forumId, final DiscussionId discussionId) {
      this(tenant, forumId, discussionId, null, null, false);
    }

    State(
            final Tenant tenant,
            final ForumId forumId,
            final DiscussionId discussionId,
            final Author author,
            final String topic,
            final boolean open) {
      this.tenant = tenant;
      this.forumId = forumId;
      this.discussionId = discussionId;
      this.author = author;
      this.topic = topic;
      this.open = open;
    }

    State closed() {
      return new State(tenant, forumId, discussionId, author, topic, false);
    }

    State opened() {
      return new State(tenant, forumId, discussionId, author, topic, true);
    }

    State withTopic(final String topic) {
      return new State(tenant, forumId, discussionId, author, topic, open);
    }
  }
}
