// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.saasovation.collaboration.infra.persistence;

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

import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.symbio.Entry.TextEntry;
import io.vlingo.symbio.EntryAdapter;
import io.vlingo.symbio.Metadata;

public class EntryAdapters {

  ////////////////////////////////////
  // Forum
  ////////////////////////////////////

  public static final class ForumStartedAdapter implements EntryAdapter<ForumStarted,TextEntry> {
    @Override
    public ForumStarted fromEntry(final TextEntry entry) {
      return JsonSerialization.deserialized(entry.entryData, ForumStarted.class);
    }

    @Override
    public TextEntry toEntry(final ForumStarted source) {
      final String serialization = JsonSerialization.serialized(source);
      return new TextEntry(ForumStarted.class, 1, serialization, Metadata.nullMetadata());
    }
  }

  public static final class ForumModeratorAssignedAdapter implements EntryAdapter<ForumModeratorAssigned,TextEntry> {
    @Override
    public ForumModeratorAssigned fromEntry(final TextEntry entry) {
      return JsonSerialization.deserialized(entry.entryData, ForumModeratorAssigned.class);
    }

    @Override
    public TextEntry toEntry(final ForumModeratorAssigned source) {
      final String serialization = JsonSerialization.serialized(source);
      return new TextEntry(ForumModeratorAssigned.class, 1, serialization, Metadata.nullMetadata());
    }
  }

  public static final class ForumClosedAdapter implements EntryAdapter<ForumClosed,TextEntry> {
    @Override
    public ForumClosed fromEntry(final TextEntry entry) {
      return JsonSerialization.deserialized(entry.entryData, ForumClosed.class);
    }

    @Override
    public TextEntry toEntry(final ForumClosed source) {
      final String serialization = JsonSerialization.serialized(source);
      return new TextEntry(ForumClosed.class, 1, serialization, Metadata.nullMetadata());
    }
  }

  public static final class ForumDescribedAdapter implements EntryAdapter<ForumDescribed,TextEntry> {
    @Override
    public ForumDescribed fromEntry(final TextEntry entry) {
      return JsonSerialization.deserialized(entry.entryData, ForumDescribed.class);
    }

    @Override
    public TextEntry toEntry(final ForumDescribed source) {
      final String serialization = JsonSerialization.serialized(source);
      return new TextEntry(ForumDescribed.class, 1, serialization, Metadata.nullMetadata());
    }
  }

  public static final class ForumReopenedAdapter implements EntryAdapter<ForumReopened,TextEntry> {
    @Override
    public ForumReopened fromEntry(final TextEntry entry) {
      return JsonSerialization.deserialized(entry.entryData, ForumReopened.class);
    }

    @Override
    public TextEntry toEntry(final ForumReopened source) {
      final String serialization = JsonSerialization.serialized(source);
      return new TextEntry(ForumReopened.class, 1, serialization, Metadata.nullMetadata());
    }
  }

  public static final class ForumTopicChangedAdapter implements EntryAdapter<ForumTopicChanged,TextEntry> {
    @Override
    public ForumTopicChanged fromEntry(final TextEntry entry) {
      return JsonSerialization.deserialized(entry.entryData, ForumTopicChanged.class);
    }

    @Override
    public TextEntry toEntry(final ForumTopicChanged source) {
      final String serialization = JsonSerialization.serialized(source);
      return new TextEntry(ForumTopicChanged.class, 1, serialization, Metadata.nullMetadata());
    }
  }

  ////////////////////////////////////
  // Discussion
  ////////////////////////////////////

  public static final class DiscussionStartedAdapter implements EntryAdapter<DiscussionStarted,TextEntry> {
    @Override
    public DiscussionStarted fromEntry(final TextEntry entry) {
      return JsonSerialization.deserialized(entry.entryData, DiscussionStarted.class);
    }

    @Override
    public TextEntry toEntry(final DiscussionStarted source) {
      final String serialization = JsonSerialization.serialized(source);
      return new TextEntry(DiscussionStarted.class, 1, serialization, Metadata.nullMetadata());
    }
  }

  public static final class DiscussionClosedAdapter implements EntryAdapter<DiscussionClosed,TextEntry> {
    @Override
    public DiscussionClosed fromEntry(final TextEntry entry) {
      return JsonSerialization.deserialized(entry.entryData, DiscussionClosed.class);
    }

    @Override
    public TextEntry toEntry(final DiscussionClosed source) {
      final String serialization = JsonSerialization.serialized(source);
      return new TextEntry(DiscussionClosed.class, 1, serialization, Metadata.nullMetadata());
    }
  }

  public static final class DiscussionReopenedAdapter implements EntryAdapter<DiscussionReopened,TextEntry> {
    @Override
    public DiscussionReopened fromEntry(final TextEntry entry) {
      return JsonSerialization.deserialized(entry.entryData, DiscussionReopened.class);
    }

    @Override
    public TextEntry toEntry(final DiscussionReopened source) {
      final String serialization = JsonSerialization.serialized(source);
      return new TextEntry(DiscussionReopened.class, 1, serialization, Metadata.nullMetadata());
    }
  }

  public static final class DiscussionTopicChangedAdapter implements EntryAdapter<DiscussionTopicChanged,TextEntry> {
    @Override
    public DiscussionTopicChanged fromEntry(final TextEntry entry) {
      return JsonSerialization.deserialized(entry.entryData, DiscussionTopicChanged.class);
    }

    @Override
    public TextEntry toEntry(final DiscussionTopicChanged source) {
      final String serialization = JsonSerialization.serialized(source);
      return new TextEntry(DiscussionTopicChanged.class, 1, serialization, Metadata.nullMetadata());
    }
  }

  ////////////////////////////////////
  // Post
  ////////////////////////////////////

  public static final class PostedToDiscussionAdapter implements EntryAdapter<PostedToDiscussion,TextEntry> {
    @Override
    public PostedToDiscussion fromEntry(final TextEntry entry) {
      return JsonSerialization.deserialized(entry.entryData, PostedToDiscussion.class);
    }

    @Override
    public TextEntry toEntry(final PostedToDiscussion source) {
      final String serialization = JsonSerialization.serialized(source);
      return new TextEntry(PostedToDiscussion.class, 1, serialization, Metadata.nullMetadata());
    }
  }

  public static final class PostModeratedAdapter implements EntryAdapter<PostModerated,TextEntry> {
    @Override
    public PostModerated fromEntry(final TextEntry entry) {
      return JsonSerialization.deserialized(entry.entryData, PostModerated.class);
    }

    @Override
    public TextEntry toEntry(final PostModerated source) {
      final String serialization = JsonSerialization.serialized(source);
      return new TextEntry(PostModerated.class, 1, serialization, Metadata.nullMetadata());
    }
  }
}
