// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.saasovation.collaboration.model.forum;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
  public void testThatDiscussionCloses() {
    System.out.println("=========== testThatDiscussionClosed ===========");
    final Tuple2<DiscussionId, Discussion> discussionPair = fixtures.discussionFixture(world);
    journalDispatcher.afterCompleting(1);
    discussionPair._2.close();
    final int count = journalDispatcher.confirmedCount(3);
    assertEquals(3, count);
    final DiscussionClosed event2 = adapter().asSource(journalDispatcher.entry(2));
    assertEquals(DiscussionClosed.class, event2.getClass());
  }

  @Test
  public void testThatDiscussionReopened() {
    System.out.println("=========== testThatDiscussionReopened ===========");
    final Tuple2<DiscussionId, Discussion> discussionPair = fixtures.discussionFixture(world);
    journalDispatcher.afterCompleting(2);
    discussionPair._2.close();
    discussionPair._2.reopen();
    final int count = journalDispatcher.confirmedCount(4);
    assertEquals(4, count);
    final DiscussionClosed event2 = adapter().asSource(journalDispatcher.entry(2));
    assertEquals(DiscussionClosed.class, event2.getClass());
    final DiscussionReopened event3 = adapter().asSource(journalDispatcher.entry(3));
    assertEquals(DiscussionReopened.class, event3.getClass());
  }

  @Test
  public void testThatDiscussionTopicChanged() {
    System.out.println("=========== testThatDiscussionTopicChanged ===========");
    final Tuple2<DiscussionId, Discussion> discussionPair = fixtures.discussionFixture(world);
    journalDispatcher.afterCompleting(1);
    final String topic = "By Way, Way, Way of Discussion";
    discussionPair._2.topicTo(topic);
    final int count = journalDispatcher.confirmedCount(3);
    assertEquals(3, count);
    final DiscussionTopicChanged event2 = adapter().asSource(journalDispatcher.entry(2));
    assertEquals(DiscussionTopicChanged.class, event2.getClass());
    assertEquals(topic, event2.topic);
  }

  @Test
  public void testThatDiscussionPosts() throws Exception {
    System.out.println("=========== testThatDiscussionPosts ===========");
    final Tuple2<DiscussionId, Discussion> discussionPair = fixtures.discussionFixture(world);
//    System.out.println("4b");
    journalDispatcher.afterCompleting(1);
//    System.out.println("4c");
    final Author author = Author.unique();
    final String subject = "Within the discussion a post";
    final String bodyText = "This is the body of the post which is document text.";
//    System.out.println("4c.1");
    Tuple2<PostId,Post> postPair = discussionPair._2.postFor(author, subject, bodyText).await(2000);
//    System.out.println("4c.2");
    assertNotNull(postPair._1);
    assertNotNull(postPair._2);
//    System.out.println("4d=" + journalDispatcher.confirmedCount());
    final int count = journalDispatcher.confirmedCount(3);
//    System.out.println("4e");
    assertEquals(3, count);
    final PostedToDiscussion event2 = postAdapter().asSource(journalDispatcher.entry(2));
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
