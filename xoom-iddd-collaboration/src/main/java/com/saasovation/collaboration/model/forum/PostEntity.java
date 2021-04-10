// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.saasovation.collaboration.model.forum;

import com.saasovation.collaboration.model.Author;
import com.saasovation.collaboration.model.Moderator;
import com.saasovation.collaboration.model.Tenant;
import com.saasovation.collaboration.model.forum.Events.PostModerated;
import com.saasovation.collaboration.model.forum.Events.PostedToDiscussion;
import io.vlingo.xoom.lattice.model.sourcing.EventSourced;

public class PostEntity extends EventSourced implements Post {
  private State state;

  public PostEntity(final Tenant tenant, final ForumId forumId, final DiscussionId discussionId, final PostId postId) {
    super(postId.toString());
    state = new State(tenant, forumId, discussionId, postId);
  }

  @Override
  public void moderate(final Moderator moderator, final String subject, final String bodyText) {
    final boolean condition1 = !moderator.equals(state.moderator);
    final boolean condition2 = !subject.equals(state.subject);
    final boolean condition3 = !bodyText.equals(state.bodyText);
    if (condition1 || condition2 || condition3) {
      apply(PostModerated.with(state.tenant, state.forumId, state.discussionId, state.postId, moderator, subject, bodyText));
    }
  }

  @Override
  public void submitWith(final Author author, final String subject, final String bodyText) {
    if (state.author == null) {
      apply(PostedToDiscussion.with(state.tenant, state.forumId, state.discussionId, state.postId, author, subject, bodyText));
    }
  }

  static {
    EventSourced.registerConsumer(PostEntity.class, PostedToDiscussion.class, PostEntity::applyPostedToDiscussion);
    EventSourced.registerConsumer(PostEntity.class, PostModerated.class, PostEntity::applyPostModerated);
  }

  private void applyPostedToDiscussion(final PostedToDiscussion e) {
    state = new State(Tenant.fromExisting(e.tenantId), ForumId.fromExisting(e.forumId), DiscussionId.fromExisting(e.discussionId), PostId.fromExisting(e.postId), Author.fromExisting(e.authorId), null, e.subject, e.bodyText);
  }

  private void applyPostModerated(final PostModerated e) {
    state = state.withModeratedContent(Moderator.fromExisting(e.moderatorId), e.subject, e.bodyText);
  }
}
