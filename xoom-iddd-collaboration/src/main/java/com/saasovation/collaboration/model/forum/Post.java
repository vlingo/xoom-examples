// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.saasovation.collaboration.model.forum;

import com.saasovation.collaboration.model.Author;
import com.saasovation.collaboration.model.Moderator;
import com.saasovation.collaboration.model.Tenant;

public interface Post {
  void moderate(final Moderator moderator, final String subject, final String bodyText);
  void submitWith(final Author author, final String subject, final String bodyText);

  public static final class State {
    public final Tenant tenant;
    public final ForumId forumId;
    public final DiscussionId discussionId;
    public final PostId postId;
    public final Author author;
    public final Moderator moderator;
    public final String subject;
    public final String bodyText;

    State(final Tenant tenant, final ForumId forumId, final DiscussionId discussionId, final PostId postId) {
      this(tenant, forumId, discussionId, postId, null, null, null, null);
    }

    State(
            final Tenant tenant,
            final ForumId forumId,
            final DiscussionId discussionId,
            final PostId postId,
            final Author author,
            final Moderator moderator,
            final String subject,
            final String bodyText) {
      this.tenant = tenant;
      this.forumId = forumId;
      this.discussionId = discussionId;
      this.postId = postId;
      this.author = author;
      this.subject = subject;
      this.bodyText = bodyText;
      this.moderator = moderator;
    }

    public static State of(String tenantId, String postId) {
      return new State(Tenant.fromExisting(tenantId), null, null, PostId.fromExisting(postId));
    }

    State withModeratedContent(final Moderator moderator, final String subject, final String bodyText) {
      return new State(tenant, forumId, discussionId, postId, author, moderator, subject, bodyText);
    }
  }
}
