// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.saasovation.collaboration.model.forum;

import org.junit.Assert;

import com.saasovation.collaboration.model.Author;
import com.saasovation.collaboration.model.Creator;
import com.saasovation.collaboration.model.MockJournalListener;
import com.saasovation.collaboration.model.Moderator;
import com.saasovation.collaboration.model.Tenant;
import com.saasovation.collaboration.model.forum.Forum.ForumDescription;

import io.vlingo.actors.World;
import io.vlingo.actors.testkit.TestUntil;
import io.vlingo.common.Tuple2;

public class Fixtures {
  public Tuple2<DiscussionId, Discussion> discussionPair;
  public Tuple2<PostId, Post> postPair;

  public ForumDescription forumDescriptionFixture() {
    return new ForumDescription(Creator.unique(), Moderator.unique(), "All Things vlingo/platform", "Discuss the vlingo/platform");
  }

  public Tuple2<DiscussionId, Discussion> discussionFixture(
          final World world,
          final MockJournalListener journalListener) {
    journalListener.until = TestUntil.happenings(1);
    final Tuple2<ForumId,Forum> forumPair = Forum.startWith(world.stage(), Tenant.unique(), forumDescriptionFixture());
    Assert.assertEquals(1, journalListener.confirmExpectedEntries(1, 10));
    journalListener.until.completes();
    journalListener.until = TestUntil.happenings(2);
    forumPair._2.discussFor(Author.unique(), "By Way of Discussion")
      .andThenConsume(discussionPair -> {
        this.discussionPair = discussionPair;
        journalListener.until.happened();
      });
    Assert.assertEquals(2, journalListener.confirmExpectedEntries(2, 20));
    journalListener.until.completes();
    return discussionPair;
  }

  public Tuple2<PostId,Post> postFixture(
          final World world,
          final MockJournalListener journalListener) {
    final Tuple2<DiscussionId, Discussion> discussionPair = discussionFixture(world, journalListener);
    journalListener.until = TestUntil.happenings(2);
    final Author author = Author.unique();
    final String subject = "Within the discussion a post";
    final String bodyText = "This is the body of the post which is document text.";
    discussionPair._2.postFor(author, subject, bodyText)
      .andThenConsume(postPair -> {
        this.postPair = postPair;
        journalListener.until.happened();
      });
    journalListener.until.completes();
    return postPair;
  }
}
