// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
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
import io.vlingo.xoom.lattice.model.sourcing.SourcedTypeRegistry;
import io.vlingo.xoom.lattice.model.sourcing.SourcedTypeRegistry.Info;
import io.vlingo.xoom.symbio.store.journal.Journal;

public class SourcedRegistration {
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public static void registerAllWith(final SourcedTypeRegistry registry, final Journal journal) {
    registry.register(new Info(journal, ForumEntity.class, ForumEntity.class.getSimpleName()))
            .register(new Info(journal, DiscussionEntity.class, DiscussionEntity.class.getSimpleName()))
            .register(new Info(journal, PostEntity.class, PostEntity.class.getSimpleName()));

    registry.info(ForumEntity.class)
            .registerEntryAdapter(ForumStarted.class, new ForumStartedAdapter())
            .registerEntryAdapter(ForumModeratorAssigned.class, new ForumModeratorAssignedAdapter())
            .registerEntryAdapter(ForumClosed.class, new ForumClosedAdapter())
            .registerEntryAdapter(ForumDescribed.class, new ForumDescribedAdapter())
            .registerEntryAdapter(ForumReopened.class, new ForumReopenedAdapter())
            .registerEntryAdapter(ForumTopicChanged.class, new ForumTopicChangedAdapter())
            .registerStateAdapter(Forum.State.class, new ForumStateAdapter());

    registry.info(DiscussionEntity.class)
            .registerEntryAdapter(DiscussionStarted.class, new DiscussionStartedAdapter())
            .registerEntryAdapter(DiscussionClosed.class, new DiscussionClosedAdapter())
            .registerEntryAdapter(DiscussionReopened.class, new DiscussionReopenedAdapter())
            .registerEntryAdapter(DiscussionTopicChanged.class, new DiscussionTopicChangedAdapter());

    registry.info(PostEntity.class)
            .registerEntryAdapter(PostedToDiscussion.class, new PostedToDiscussionAdapter())
            .registerEntryAdapter(PostModerated.class, new PostModeratedAdapter());
  }
}
