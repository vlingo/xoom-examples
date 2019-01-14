// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.saasovation.collaboration.model.forum;

import com.saasovation.collaboration.model.Author;
import com.saasovation.collaboration.model.Creator;
import com.saasovation.collaboration.model.Moderator;
import com.saasovation.collaboration.model.Tenant;

import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;
import io.vlingo.common.Tuple2;

public interface Forum {
  static Tuple2<ForumId, Forum> startWith(
          final Stage stage,
          final Tenant tenant,
          final ForumDescription description) {
    final Forum forum = stage.actorFor(Forum.class, ForumEntity.class, tenant, description.forumId);
    forum.startWith(description.creator, description.moderator, description.topic, description.description, description.exclusiveOwner);
    return Tuple2.from(description.forumId, forum);
  }

  void assign(final Moderator moderator);
  void close();
  void describeAs(final String description);
  Completes<Tuple2<DiscussionId, Discussion>> discussFor(final Author author, final String topic);
  void reopen();
  void startWith(final Creator creator, final Moderator moderator, final String topic, final String description, final String exclusiveOwner);
  void topicIs(final String topic);

  public static final class ForumDescription {
    public final ForumId forumId;
    public final Creator creator;
    public final Moderator moderator;
    public final String topic;
    public final String description;
    public final String exclusiveOwner;

    public ForumDescription(final Creator creator, final Moderator moderator, final String topic, final String description) {
      this(creator, moderator, topic, description, null);
    }

    public ForumDescription(final Creator creator, final Moderator moderator, final String topic, final String description, final String exclusiveOwner) {
      this.creator = creator;
      this.moderator = moderator;
      this.topic = topic;
      this.description = description;
      this.exclusiveOwner = exclusiveOwner;
      this.forumId = ForumId.unique();
    }
  }
}
