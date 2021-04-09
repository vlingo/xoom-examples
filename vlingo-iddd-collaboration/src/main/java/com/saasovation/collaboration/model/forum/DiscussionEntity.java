// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.saasovation.collaboration.model.forum;

import com.saasovation.collaboration.model.Author;
import com.saasovation.collaboration.model.Tenant;
import com.saasovation.collaboration.model.forum.Events.DiscussionClosed;
import com.saasovation.collaboration.model.forum.Events.DiscussionReopened;
import com.saasovation.collaboration.model.forum.Events.DiscussionStarted;
import com.saasovation.collaboration.model.forum.Events.DiscussionTopicChanged;

import io.vlingo.common.Completes;
import io.vlingo.common.Tuple2;
import io.vlingo.lattice.model.sourcing.EventSourced;

public class DiscussionEntity extends EventSourced implements Discussion {
  private State state;

  public DiscussionEntity(final Tenant tenant, final ForumId forumId, final DiscussionId discussionId) {
    super(discussionId.value);
    state = new State(tenant, forumId, discussionId);
  }

  @Override
  public void close() {
    if (state.open) {
      apply(DiscussionClosed.with(state.tenant, state.forumId, state.discussionId));
    }
  }

  @Override
  public Completes<Tuple2<PostId,Post>> postFor(final Author author, final String subject, final String bodyText) {
    final PostId postId = PostId.unique();
    final Post post = stage().actorFor(Post.class, PostEntity.class, state.tenant, state.forumId, state.discussionId, postId);
    post.submitWith(author, subject, bodyText);
    return completes().with(Tuple2.from(postId, post));
  }

  @Override
  public void reopen() {
    if (!state.open) {
      apply(DiscussionReopened.with(state.tenant, state.forumId, state.discussionId));
    }
  }

  @Override
  public void startWith(final Author author, final String topic, final String ownerId) {
    if (state.author == null) {
      apply(DiscussionStarted.with(state.tenant, state.forumId, state.discussionId, author, topic, ownerId));
    }
  }

  @Override
  public void topicTo(final String topic) {
    if (!state.topic.equals(topic)) {
      apply(DiscussionTopicChanged.with(state.tenant, state.forumId, state.discussionId, topic));
    }
  }

  static {
    EventSourced.registerConsumer(DiscussionEntity.class, DiscussionStarted.class, DiscussionEntity::applyDiscussionStarted);
    EventSourced.registerConsumer(DiscussionEntity.class, DiscussionClosed.class, DiscussionEntity::applyDiscussionClosed);
    EventSourced.registerConsumer(DiscussionEntity.class, DiscussionReopened.class, DiscussionEntity::applyDiscussionReopened);
    EventSourced.registerConsumer(DiscussionEntity.class, DiscussionTopicChanged.class, DiscussionEntity::applyDiscussionTopicChanged);
  }

  private void applyDiscussionStarted(final DiscussionStarted e) {
    state = new State(Tenant.fromExisting(e.tenantId), ForumId.fromExisting(e.forumId), DiscussionId.fromExisting(e.discussionId), Author.fromExisting(e.authorId), e.topic, true);
  }

  private void applyDiscussionClosed(final DiscussionClosed e) {
    state = state.closed();
  }

  private void applyDiscussionReopened(final DiscussionReopened e) {
    state = state.opened();
  }

  private void applyDiscussionTopicChanged(final DiscussionTopicChanged e) {
    state = state.withTopic(e.topic);
  }
}
