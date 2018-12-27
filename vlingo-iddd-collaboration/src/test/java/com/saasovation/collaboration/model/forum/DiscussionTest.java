// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.saasovation.collaboration.model.forum;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.saasovation.collaboration.model.Author;
import com.saasovation.collaboration.model.EntityTest;
import com.saasovation.collaboration.model.forum.Events.DiscussionClosed;
import com.saasovation.collaboration.model.forum.Events.DiscussionReopened;
import com.saasovation.collaboration.model.forum.Events.DiscussionTopicChanged;
import com.saasovation.collaboration.model.forum.Events.PostedToDiscussion;

import io.vlingo.common.Tuple2;
import io.vlingo.symbio.EntryAdapterProvider;

public class DiscussionTest extends EntityTest {
  private Fixtures fixtures = new Fixtures();

  @Test
  public void testThatDiscussionClosed() {
    final Tuple2<DiscussionId, Discussion> discussionPair = fixtures.discussionFixture(world, journalListener);
    this.journalListener.until = until(1);
    discussionPair._2.close();
    journalListener.until.completes();
    assertEquals(3, journalListener.confirmExpectedEntries(3, 10));
    final DiscussionClosed event2 = adapter().asSource(journalListener.allEntries.get(2));
    assertEquals(DiscussionClosed.class, event2.getClass());
  }

  @Test
  public void testThatDiscussionReopened() {
    final Tuple2<DiscussionId, Discussion> discussionPair = fixtures.discussionFixture(world, journalListener);
    this.journalListener.until = until(2);
    discussionPair._2.close();
    discussionPair._2.reopen();
    journalListener.until.completes();
    assertEquals(4, journalListener.confirmExpectedEntries(4, 10));
    final DiscussionClosed event2 = adapter().asSource(journalListener.allEntries.get(2));
    assertEquals(DiscussionClosed.class, event2.getClass());
    final DiscussionReopened event3 = adapter().asSource(journalListener.allEntries.get(3));
    assertEquals(DiscussionReopened.class, event3.getClass());
  }

  @Test
  public void testThatDiscussionTopicChanged() {
    final Tuple2<DiscussionId, Discussion> discussionPair = fixtures.discussionFixture(world, journalListener);
    this.journalListener.until = until(1);
    final String topic = "By Way, Way, Way of Discussion";
    discussionPair._2.topicTo(topic);
    journalListener.until.completes();
    assertEquals(3, journalListener.confirmExpectedEntries(3, 10));
    final DiscussionTopicChanged event2 = adapter().asSource(journalListener.allEntries.get(2));
    assertEquals(DiscussionTopicChanged.class, event2.getClass());
    assertEquals(topic, event2.topic);
  }

  @Test
  public void testThatDiscussionPosts() {
    final Tuple2<DiscussionId, Discussion> discussionPair = fixtures.discussionFixture(world, journalListener);
    this.journalListener.until = until(1);
    final Author author = Author.unique();
    final String subject = "Within the discussion a post";
    final String bodyText = "This is the body of the post which is document text.";
    discussionPair._2.postFor(author, subject, bodyText);
    journalListener.until.completes();
    assertEquals(3, journalListener.confirmExpectedEntries(3, 10));
    final PostedToDiscussion event2 = postAdapter().asSource(journalListener.allEntries.get(2));
    assertEquals(PostedToDiscussion.class, event2.getClass());
    assertEquals(author.value, event2.authorId);
    assertEquals(subject, event2.subject);
    assertEquals(bodyText, event2.bodyText);
  }

  private EntryAdapterProvider adapter() {
    return registry.info(DiscussionEntity.class).entryAdapterProvider;
  }

  private EntryAdapterProvider postAdapter() {
    return registry.info(PostEntity.class).entryAdapterProvider;
  }
}
