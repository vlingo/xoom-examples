// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.saasovation.collaboration.model.forum;

import java.util.function.BiConsumer;

import com.saasovation.collaboration.model.Author;
import com.saasovation.collaboration.model.Creator;
import com.saasovation.collaboration.model.Moderator;
import com.saasovation.collaboration.model.Tenant;
import com.saasovation.collaboration.model.forum.Events.ForumClosed;
import com.saasovation.collaboration.model.forum.Events.ForumDescribed;
import com.saasovation.collaboration.model.forum.Events.ForumModeratorAssigned;
import com.saasovation.collaboration.model.forum.Events.ForumReopened;
import com.saasovation.collaboration.model.forum.Events.ForumStarted;
import com.saasovation.collaboration.model.forum.Events.ForumTopicChanged;

import io.vlingo.common.Completes;
import io.vlingo.common.Tuple2;
import io.vlingo.lattice.model.sourcing.EventSourced;

public class ForumEntity extends EventSourced<String> implements Forum {
  private State state;

  public ForumEntity(final Tenant tenant, final ForumId forumId) {
    state = new State(tenant, forumId);
  }

  @Override
  public void assign(final Moderator moderator) {
    if (!moderator.equals(state.moderator)) {
      apply(ForumModeratorAssigned.with(state.tenant, state.forumId, moderator, state.exclusiveOwner));
    }
  }

  @Override
  public void close() {
    if (state.open) {
      apply(ForumClosed.with(state.tenant, state.forumId, state.exclusiveOwner));
    }
  }

  @Override
  public void describeAs(final String description) {
    if (!description.equals(state.description)) {
      apply(ForumDescribed.with(state.tenant, state.forumId, description, state.exclusiveOwner));
    }
  }

  @Override
  public Completes<Tuple2<DiscussionId, Discussion>> discussFor(final Author author, final String topic) {
    final DiscussionId discussionId = DiscussionId.unique();
    final Discussion discussion = stage().actorFor(Discussion.class, DiscussionEntity.class, state.tenant, state.forumId, discussionId);
    discussion.startWith(author, topic);
    return completes().with(Tuple2.from(discussionId, discussion));
  }

  @Override
  public void reopen() {
    if (!state.open) {
      apply(ForumReopened.with(state.tenant, state.forumId, state.exclusiveOwner));
    }
  }

  @Override
  public void startWith(final Creator creator, final Moderator moderator, final String topic, final String description, final String exclusiveOwner) {
    if (state.creator == null) {
      apply(ForumStarted.with(state.tenant, state.forumId, creator, moderator, topic, description, exclusiveOwner));
    }
  }

  @Override
  public void topicIs(final String topic) {
    if (!topic.equals(state.topic)) {
      apply(ForumTopicChanged.with(state.tenant, state.forumId, topic, state.exclusiveOwner));
    }
  }

  @Override
  protected String streamName() {
    return streamNameFrom(":", state.tenant.value, state.forumId.value);
  }

  private final class State {
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

  static {
    BiConsumer<ForumEntity, ForumStarted> applyForumStartedFn = ForumEntity::applyForumStarted;
    EventSourced.registerConsumer(ForumEntity.class, ForumStarted.class, applyForumStartedFn);
    BiConsumer<ForumEntity, ForumModeratorAssigned> applyForumModeratorAssignedFn = ForumEntity::applyForumModeratorAssigned;
    EventSourced.registerConsumer(ForumEntity.class, ForumModeratorAssigned.class, applyForumModeratorAssignedFn);
    BiConsumer<ForumEntity, ForumClosed> applyForumClosedFn = ForumEntity::applyForumClosed;
    EventSourced.registerConsumer(ForumEntity.class, ForumClosed.class, applyForumClosedFn);
    BiConsumer<ForumEntity, ForumDescribed> applyForumDescribedFn = ForumEntity::applyForumDescribed;
    EventSourced.registerConsumer(ForumEntity.class, ForumDescribed.class, applyForumDescribedFn);
    BiConsumer<ForumEntity, ForumReopened> applyForumReopenedFn = ForumEntity::applyForumReopened;
    EventSourced.registerConsumer(ForumEntity.class, ForumReopened.class, applyForumReopenedFn);
    BiConsumer<ForumEntity, ForumTopicChanged> applyForumTopicChangedFn = ForumEntity::applyForumTopicChanged;
    EventSourced.registerConsumer(ForumEntity.class, ForumTopicChanged.class, applyForumTopicChangedFn);
  }

  private void applyForumStarted(final ForumStarted e) {
    state = new State(Tenant.fromExisting(e.tenantId), ForumId.fromExisting(e.forumId), Creator.fromExisting(e.creatorId), Moderator.fromExisting(e.moderatorId), e.topic, e.description, e.exclusiveOwner, true);
  }

  private void applyForumModeratorAssigned(final ForumModeratorAssigned e) {
    state = state.withModerator(Moderator.fromExisting(e.moderatorId));
  }

  private void applyForumClosed(final ForumClosed e) {
    state = state.closed();
  }

  private void applyForumDescribed(final ForumDescribed e) {
    state = state.withDescription(e.description);
  }

  private void applyForumReopened(final ForumReopened e) {
    state = state.opened();
  }

  private void applyForumTopicChanged(final ForumTopicChanged e) {
    state = state.withTopic(e.topic);
  }
}
