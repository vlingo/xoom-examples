// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
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

import io.vlingo.actors.Definition;
import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;
import io.vlingo.common.Tuple2;

public interface Forum {
  static Tuple2<ForumId, Forum> startWith(
          final Stage stage,
          final Tenant tenant,
          final ForumDescription description) {
    final Forum forum = stage.actorFor(Forum.class, Definition.has(ForumEntity.class, Definition.parameters(tenant, description.forumId)));
    forum.startWith(description.creator, description.moderator, description.topic, description.description, description.exclusiveOwner);
    return Tuple2.from(description.forumId, forum);
  }

  void assign(final Moderator moderator);
  void close();
  void describeAs(final String description);
  Completes<Tuple2<DiscussionId, Discussion>> discussFor(final Author author, final String topic, final String ownerId);
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

  public static final class State {
    public final Tenant tenant;
    public final ForumId forumId;
    public final Creator creator;
    public final Moderator moderator;
    public final String topic;
    public final String description;
    public final String exclusiveOwner;
    public final boolean open;

    State(final Tenant tenant, final ForumId forumId) {
      this(tenant, forumId, null, null, null, null, null, false);
    }

    State(
            final Tenant tenant,
            final ForumId forumId,
            final Creator creator,
            final Moderator moderator,
            final String topic,
            final String description,
            final String exclusiveOwner,
            final boolean open) {
      this.tenant = tenant;
      this.forumId = forumId;
      this.creator = creator;
      this.moderator = moderator;
      this.topic = topic;
      this.description = description;
      this.exclusiveOwner = exclusiveOwner;
      this.open = open;
    }

    @Override
    public String toString() {
      return "[" + tenant + " " + forumId + " " + moderator + " topic=\"" + topic + "\" description=\" " + description + " exclusiveOwner=" + exclusiveOwner + " open=" + open + "]";
    }

    State closed() {
      return new State(tenant, forumId, creator, moderator, topic, description, exclusiveOwner, false);
    }

    State opened() {
      return new State(tenant, forumId, creator, moderator, topic, description, exclusiveOwner, true);
    }

    State withDescription(final String description) {
      return new State(tenant, forumId, creator, moderator, topic, description, exclusiveOwner, open);
    }

    State withModerator(final Moderator moderator) {
      return new State(tenant, forumId, creator, moderator, topic, description, exclusiveOwner, open);
    }

    State withTopic(final String topic) {
      return new State(tenant, forumId, creator, moderator, topic, description, exclusiveOwner, open);
    }
  }
}
