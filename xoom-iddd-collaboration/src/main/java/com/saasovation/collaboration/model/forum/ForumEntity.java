// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
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
import com.saasovation.collaboration.model.forum.Events.*;
import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.common.Tuple2;
import io.vlingo.xoom.lattice.model.sourcing.EventSourced;

public class ForumEntity extends EventSourced implements Forum {
  private State state;

  public ForumEntity(final Tenant tenant, final ForumId forumId) {
    super(forumId.value);
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
  public Completes<Tuple2<DiscussionId, Discussion>> discussFor(final Author author, final String topic, final String ownerId) {
    final DiscussionId discussionId = DiscussionId.unique();
    final Discussion discussion = stage().actorFor(Discussion.class, DiscussionEntity.class, state.tenant, state.forumId, discussionId);
    discussion.startWith(author, topic, ownerId);
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
  @SuppressWarnings("unchecked")
  protected State snapshot() {
    if (currentVersion() % 100 == 0) {
      return state;
    }
    return null;
  }

  static {
    EventSourced.registerConsumer(ForumEntity.class, ForumStarted.class, ForumEntity::applyForumStarted);
    EventSourced.registerConsumer(ForumEntity.class, ForumModeratorAssigned.class, ForumEntity::applyForumModeratorAssigned);
    EventSourced.registerConsumer(ForumEntity.class, ForumClosed.class, ForumEntity::applyForumClosed);
    EventSourced.registerConsumer(ForumEntity.class, ForumDescribed.class, ForumEntity::applyForumDescribed);
    EventSourced.registerConsumer(ForumEntity.class, ForumReopened.class, ForumEntity::applyForumReopened);
    EventSourced.registerConsumer(ForumEntity.class, ForumTopicChanged.class, ForumEntity::applyForumTopicChanged);
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
