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

import io.vlingo.lattice.model.DomainEvent;

public class Events {
  public static final class ForumStarted extends DomainEvent {
    public final String tenantId;
    public final String forumId;
    public final String creatorId;
    public final String moderatorId;
    public final String topic;
    public String description;
    public String exclusiveOwner;

    public static ForumStarted with(final Tenant tenant, final ForumId forumId, final Creator creator,
            final Moderator moderator, final String topic, String description, String exclusiveOwner) {
      return new ForumStarted(tenant, forumId, creator, moderator, topic, description, exclusiveOwner);
    }

    public ForumStarted(final Tenant tenant, final ForumId forumId, final Creator creator,
            final Moderator moderator, final String topic, String description, String exclusiveOwner) {
      this.tenantId = tenant.value;
      this.forumId = forumId.value;
      this.creatorId = creator.value;
      this.moderatorId = moderator.value;
      this.topic = topic;
      this.description = description;
      this.exclusiveOwner = exclusiveOwner;
    }
  }

  public static final class ForumModeratorAssigned extends DomainEvent {
    public final String tenantId;
    public final String forumId;
    public final String moderatorId;
    public final String exclusiveOwner;

    public static ForumModeratorAssigned with(final Tenant tenant, final ForumId forumId, final Moderator moderator, final String exclusiveOwner) {
      return new ForumModeratorAssigned(tenant, forumId, moderator, exclusiveOwner);
    }

    public ForumModeratorAssigned(final Tenant tenant, final ForumId forumId, final Moderator moderator, final String exclusiveOwner) {
      this.tenantId = tenant.value;
      this.forumId = forumId.value;
      this.moderatorId = moderator.value;
      this.exclusiveOwner = exclusiveOwner;
    }
  }

  public static final class ForumClosed extends DomainEvent {
    public final String tenantId;
    public final String forumId;
    public final String exclusiveOwner;

    public static ForumClosed with(final Tenant tenant, final ForumId forumId, final String exclusiveOwner) {
      return new ForumClosed(tenant, forumId, exclusiveOwner);
    }

    public ForumClosed(final Tenant tenant, final ForumId forumId, final String exclusiveOwner) {
      this.tenantId = tenant.value;
      this.forumId = forumId.value;
      this.exclusiveOwner = exclusiveOwner;
    }
  }

  public static final class ForumDescribed extends DomainEvent {
    public final String tenantId;
    public final String forumId;
    public final String description;
    public final String exclusiveOwner;

    public static ForumDescribed with(final Tenant tenant, final ForumId forumId, final String description, final String exclusiveOwner) {
      return new ForumDescribed(tenant, forumId, description, exclusiveOwner);
    }

    public ForumDescribed(final Tenant tenant, final ForumId forumId, final String description, final String exclusiveOwner) {
      this.tenantId = tenant.value;
      this.forumId = forumId.value;
      this.description = description;
      this.exclusiveOwner = exclusiveOwner;
    }
  }

  public static final class ForumReopened extends DomainEvent {
    public final String tenantId;
    public final String forumId;
    public final String exclusiveOwner;

    public static ForumReopened with(final Tenant tenant, final ForumId forumId, final String exclusiveOwner) {
      return new ForumReopened(tenant, forumId, exclusiveOwner);
    }

    public ForumReopened(final Tenant tenant, final ForumId forumId, final String exclusiveOwner) {
      this.tenantId = tenant.value;
      this.forumId = forumId.value;
      this.exclusiveOwner = exclusiveOwner;
    }
  }

  public static final class ForumTopicChanged extends DomainEvent {
    public final String tenantId;
    public final String forumId;
    public final String topic;
    public final String exclusiveOwner;

    public static ForumTopicChanged with(final Tenant tenant, final ForumId forumId, final String topic, final String exclusiveOwner) {
      return new ForumTopicChanged(tenant, forumId, topic, exclusiveOwner);
    }

    public ForumTopicChanged(final Tenant tenant, final ForumId forumId, final String topic, final String exclusiveOwner) {
      this.tenantId = tenant.value;
      this.forumId = forumId.value;
      this.topic = topic;
      this.exclusiveOwner = exclusiveOwner;
    }
  }

  public static final class DiscussionStarted extends DomainEvent {
    public final String tenantId;
    public final String forumId;
    public final String discussionId;
    public final String authorId;
    public final String topic;

    public static DiscussionStarted with(final Tenant tenant, final ForumId forumId, final DiscussionId discussionId, final Author author, final String topic) {
      return new DiscussionStarted(tenant, forumId, discussionId, author, topic);
    }

    public DiscussionStarted(final Tenant tenant, final ForumId forumId, final DiscussionId discussionId, final Author author, final String topic) {
      this.tenantId = tenant.value;
      this.forumId = forumId.value;
      this.discussionId = discussionId.value;
      this.authorId = author.value;
      this.topic = topic;
    }
  }

  public static final class DiscussionClosed extends DomainEvent {
    public final String tenantId;
    public final String forumId;
    public final String discussionId;

    public static DiscussionClosed with(final Tenant tenant, final ForumId forumId, final DiscussionId discussionId) {
      return new DiscussionClosed(tenant, forumId, discussionId);
    }

    public DiscussionClosed(final Tenant tenant, final ForumId forumId, final DiscussionId discussionId) {
      this.tenantId = tenant.value;
      this.forumId = forumId.value;
      this.discussionId = discussionId.value;
    }
  }

  public static final class DiscussionReopened extends DomainEvent {
    public final String tenantId;
    public final String forumId;
    public final String discussionId;

    public static DiscussionReopened with(final Tenant tenant, final ForumId forumId, final DiscussionId discussionId) {
      return new DiscussionReopened(tenant, forumId, discussionId);
    }

    public DiscussionReopened(final Tenant tenant, final ForumId forumId, final DiscussionId discussionId) {
      this.tenantId = tenant.value;
      this.forumId = forumId.value;
      this.discussionId = discussionId.value;
    }
  }

  public static final class DiscussionTopicChanged extends DomainEvent {
    public final String tenantId;
    public final String forumId;
    public final String discussionId;
    public final String topic;

    public static DiscussionTopicChanged with(final Tenant tenant, final ForumId forumId, final DiscussionId discussionId, final String topic) {
      return new DiscussionTopicChanged(tenant, forumId, discussionId, topic);
    }

    public DiscussionTopicChanged(final Tenant tenant, final ForumId forumId, final DiscussionId discussionId, final String topic) {
      this.tenantId = tenant.value;
      this.forumId = forumId.value;
      this.discussionId = discussionId.value;
      this.topic = topic;
    }
  }
}
