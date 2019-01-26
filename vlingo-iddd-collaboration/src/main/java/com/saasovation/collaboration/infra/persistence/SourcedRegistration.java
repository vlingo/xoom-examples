// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.saasovation.collaboration.infra.persistence;

import com.saasovation.collaboration.infra.persistence.EntryAdapters.DiscussionClosedAdapter;
import com.saasovation.collaboration.infra.persistence.EntryAdapters.DiscussionReopenedAdapter;
import com.saasovation.collaboration.infra.persistence.EntryAdapters.DiscussionStartedAdapter;
import com.saasovation.collaboration.infra.persistence.EntryAdapters.DiscussionTopicChangedAdapter;
import com.saasovation.collaboration.infra.persistence.EntryAdapters.ForumClosedAdapter;
import com.saasovation.collaboration.infra.persistence.EntryAdapters.ForumDescribedAdapter;
import com.saasovation.collaboration.infra.persistence.EntryAdapters.ForumModeratorAssignedAdapter;
import com.saasovation.collaboration.infra.persistence.EntryAdapters.ForumReopenedAdapter;
import com.saasovation.collaboration.infra.persistence.EntryAdapters.ForumStartedAdapter;
import com.saasovation.collaboration.infra.persistence.EntryAdapters.ForumTopicChangedAdapter;
import com.saasovation.collaboration.infra.persistence.EntryAdapters.PostModeratedAdapter;
import com.saasovation.collaboration.infra.persistence.EntryAdapters.PostedToDiscussionAdapter;
import com.saasovation.collaboration.infra.persistence.SnapshotStateAdapters.ForumStateAdapter;
import com.saasovation.collaboration.model.forum.DiscussionEntity;
import com.saasovation.collaboration.model.forum.Events.DiscussionClosed;
import com.saasovation.collaboration.model.forum.Events.DiscussionReopened;
import com.saasovation.collaboration.model.forum.Events.DiscussionStarted;
import com.saasovation.collaboration.model.forum.Events.DiscussionTopicChanged;
import com.saasovation.collaboration.model.forum.Events.ForumClosed;
import com.saasovation.collaboration.model.forum.Events.ForumDescribed;
import com.saasovation.collaboration.model.forum.Events.ForumModeratorAssigned;
import com.saasovation.collaboration.model.forum.Events.ForumReopened;
import com.saasovation.collaboration.model.forum.Events.ForumStarted;
import com.saasovation.collaboration.model.forum.Events.ForumTopicChanged;
import com.saasovation.collaboration.model.forum.Events.PostModerated;
import com.saasovation.collaboration.model.forum.Events.PostedToDiscussion;
import com.saasovation.collaboration.model.forum.Forum;
import com.saasovation.collaboration.model.forum.ForumEntity;
import com.saasovation.collaboration.model.forum.PostEntity;

import io.vlingo.lattice.model.sourcing.SourcedTypeRegistry;
import io.vlingo.lattice.model.sourcing.SourcedTypeRegistry.Info;
import io.vlingo.symbio.store.journal.Journal;

public class SourcedRegistration {
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public static <T> void registerAllWith(final SourcedTypeRegistry registry, final Journal<T> journal) {
    registry
      .register(new Info(journal, ForumEntity.class, ForumEntity.class.getSimpleName()))
      .register(new Info(journal, DiscussionEntity.class, DiscussionEntity.class.getSimpleName()))
      .register(new Info(journal, PostEntity.class, PostEntity.class.getSimpleName()));

    registry.info(ForumEntity.class)
      .registerEntryAdapter(ForumStarted.class, new ForumStartedAdapter(),
              (type, adapter) -> journal.registerEntryAdapter(type, adapter))
      .registerEntryAdapter(ForumModeratorAssigned.class, new ForumModeratorAssignedAdapter(),
              (type, adapter) -> journal.registerEntryAdapter(type, adapter))
      .registerEntryAdapter(ForumClosed.class, new ForumClosedAdapter(),
              (type, adapter) -> journal.registerEntryAdapter(type, adapter))
      .registerEntryAdapter(ForumDescribed.class, new ForumDescribedAdapter(),
              (type, adapter) -> journal.registerEntryAdapter(type, adapter))
      .registerEntryAdapter(ForumReopened.class, new ForumReopenedAdapter(),
              (type, adapter) -> journal.registerEntryAdapter(type, adapter))
      .registerEntryAdapter(ForumTopicChanged.class, new ForumTopicChangedAdapter(),
              (type, adapter) -> journal.registerEntryAdapter(type, adapter))
      .registerStateAdapter(Forum.State.class, new ForumStateAdapter(),
              (type, adapter) -> journal.registerStateAdapter(type, adapter));

    registry.info(DiscussionEntity.class)
      .registerEntryAdapter(DiscussionStarted.class, new DiscussionStartedAdapter(),
              (type, adapter) -> journal.registerEntryAdapter(type, adapter))
      .registerEntryAdapter(DiscussionClosed.class, new DiscussionClosedAdapter(),
              (type, adapter) -> journal.registerEntryAdapter(type, adapter))
      .registerEntryAdapter(DiscussionReopened.class, new DiscussionReopenedAdapter(),
              (type, adapter) -> journal.registerEntryAdapter(type, adapter))
      .registerEntryAdapter(DiscussionTopicChanged.class, new DiscussionTopicChangedAdapter(),
              (type, adapter) -> journal.registerEntryAdapter(type, adapter));

    registry.info(PostEntity.class)
      .registerEntryAdapter(PostedToDiscussion.class, new PostedToDiscussionAdapter(),
              (type, adapter) -> journal.registerEntryAdapter(type, adapter))
      .registerEntryAdapter(PostModerated.class, new PostModeratedAdapter(),
              (type, adapter) -> journal.registerEntryAdapter(type, adapter));
  }
}
