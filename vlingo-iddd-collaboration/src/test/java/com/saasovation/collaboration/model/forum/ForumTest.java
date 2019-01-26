// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.saasovation.collaboration.model.forum;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.UUID;

import org.junit.Test;

import com.saasovation.collaboration.model.Author;
import com.saasovation.collaboration.model.EntityTest;
import com.saasovation.collaboration.model.Moderator;
import com.saasovation.collaboration.model.Tenant;
import com.saasovation.collaboration.model.forum.Events.DiscussionStarted;
import com.saasovation.collaboration.model.forum.Events.ForumClosed;
import com.saasovation.collaboration.model.forum.Events.ForumDescribed;
import com.saasovation.collaboration.model.forum.Events.ForumModeratorAssigned;
import com.saasovation.collaboration.model.forum.Events.ForumReopened;
import com.saasovation.collaboration.model.forum.Events.ForumStarted;
import com.saasovation.collaboration.model.forum.Events.ForumTopicChanged;
import com.saasovation.collaboration.model.forum.Forum.ForumDescription;

import io.vlingo.common.Tuple2;
import io.vlingo.symbio.EntryAdapterProvider;

public class ForumTest extends EntityTest {
  private Fixtures fixtures = new Fixtures();

  @Test
  public void testThatForumStarted() {
    this.journalListener.until = until(1);
    final ForumDescription description = fixtures.forumDescriptionFixture();
    final Tuple2<ForumId,Forum> forumPair = Forum.startWith(world.stage(), Tenant.unique(), description);
    assertNotNull(forumPair);
    assertNotNull(forumPair._1);
    UUID.fromString(forumPair._1.value); // throws if not correct format
    assertNotNull(forumPair._2);
    journalListener.until.completes();
    journalListener.confirmExpectedEntries(1, 10);
    assertEquals(1, journalListener.confirmedCount.get().intValue());
    final ForumStarted event = adapter().asSource(journalListener.allEntries.get(0));
    assertEquals(ForumStarted.class, event.getClass());
    assertEquals(description.creator.value, event.creatorId);
    assertEquals(description.description, event.description);
    assertEquals(description.exclusiveOwner, event.exclusiveOwner);
    assertEquals(description.forumId.value, event.forumId);
    assertEquals(description.moderator.value, event.moderatorId);
    assertEquals(description.topic, event.topic);
  }

  @Test
  public void testThatForumModeratorAssigned() {
    this.journalListener.until = until(2);
    final Tuple2<ForumId,Forum> forumPair = Forum.startWith(world.stage(), Tenant.unique(), fixtures.forumDescriptionFixture());
    final Moderator moderator = Moderator.unique();
    forumPair._2.assign(moderator);
    journalListener.until.completes();
    journalListener.confirmExpectedEntries(2, 10);
    assertEquals(2, journalListener.confirmedCount.get().intValue());
    final ForumStarted event0 = adapter().asSource(journalListener.allEntries.get(0));
    assertEquals(ForumStarted.class, event0.getClass());
    final ForumModeratorAssigned event1 = adapter().asSource(journalListener.allEntries.get(1));
    assertEquals(ForumModeratorAssigned.class, event1.getClass());
    assertEquals(moderator.value, event1.moderatorId);
  }

  @Test
  public void testThatForumClosed() {
    this.journalListener.until = until(2);
    final Tuple2<ForumId,Forum> forumPair = Forum.startWith(world.stage(), Tenant.unique(), fixtures.forumDescriptionFixture());
    forumPair._2.close();
    journalListener.until.completes();
    journalListener.confirmExpectedEntries(2, 10);
    assertEquals(2, journalListener.confirmedCount.get().intValue());
    final ForumStarted event0 = adapter().asSource(journalListener.allEntries.get(0));
    assertEquals(ForumStarted.class, event0.getClass());
    final ForumClosed event1 = adapter().asSource(journalListener.allEntries.get(1));
    assertEquals(ForumClosed.class, event1.getClass());
  }

  @Test
  public void testThatForumDescribed() {
    this.journalListener.until = until(2);
    final Tuple2<ForumId,Forum> forumPair = Forum.startWith(world.stage(), Tenant.unique(), fixtures.forumDescriptionFixture());
    final String description = "Let's discuss the vlingo/platform";
    forumPair._2.describeAs(description);
    journalListener.until.completes();
    journalListener.confirmExpectedEntries(2, 10);
    assertEquals(2, journalListener.confirmedCount.get().intValue());
    final ForumStarted event0 = adapter().asSource(journalListener.allEntries.get(0));
    assertEquals(ForumStarted.class, event0.getClass());
    final ForumDescribed event1 = adapter().asSource(journalListener.allEntries.get(1));
    assertEquals(ForumDescribed.class, event1.getClass());
    assertEquals(description, event1.description);
  }

  @Test
  public void testThatForumReopened() {
    this.journalListener.until = until(1);
    final Tuple2<ForumId,Forum> forumPair = Forum.startWith(world.stage(), Tenant.unique(), fixtures.forumDescriptionFixture());
    journalListener.until.completes();
    journalListener.confirmExpectedEntries(1, 10);
    assertEquals(1, journalListener.confirmedCount.get().intValue());
    this.journalListener.until = until(1);
    forumPair._2.close();
    journalListener.until.completes();
    journalListener.confirmExpectedEntries(2, 10);
    assertEquals(2, journalListener.confirmedCount.get().intValue());
    this.journalListener.until = until(1);
    forumPair._2.reopen();
    journalListener.until.completes();
    journalListener.confirmExpectedEntries(3, 10);
    assertEquals(3, journalListener.confirmedCount.get().intValue());
    final ForumStarted event0 = adapter().asSource(journalListener.allEntries.get(0));
    assertEquals(ForumStarted.class, event0.getClass());
    final ForumClosed event1 = adapter().asSource(journalListener.allEntries.get(1));
    assertEquals(ForumClosed.class, event1.getClass());
    final ForumReopened event2 = adapter().asSource(journalListener.allEntries.get(2));
    assertEquals(ForumReopened.class, event2.getClass());
  }

  @Test
  public void testThatForumTopicChanged() {
    this.journalListener.until = until(2);
    final Tuple2<ForumId,Forum> forumPair = Forum.startWith(world.stage(), Tenant.unique(), fixtures.forumDescriptionFixture());
    final String topic = "Even More Than All Things vlingo/platform";
    forumPair._2.topicIs(topic);
    journalListener.until.completes();
    journalListener.confirmExpectedEntries(2, 10);
    assertEquals(2, journalListener.confirmedCount.get().intValue());
    final ForumStarted event0 = adapter().asSource(journalListener.allEntries.get(0));
    assertEquals(ForumStarted.class, event0.getClass());
    final ForumTopicChanged event1 = adapter().asSource(journalListener.allEntries.get(1));
    assertEquals(ForumTopicChanged.class, event1.getClass());
    assertEquals(topic, event1.topic);
  }

  @Test
  public void testThatForumStartsDiscussion() {
    this.journalListener.until = until(2);
    final Tuple2<ForumId,Forum> forumPair = Forum.startWith(world.stage(), Tenant.unique(), fixtures.forumDescriptionFixture());
    final Author author = Author.unique();
    final String topic = "At Topic That Is of Great Interest";
    forumPair._2.discussFor(author, topic)
      .andThenConsume(discussionPair -> {
        assertNotNull(discussionPair);
        assertNotNull(discussionPair._1);
        UUID.fromString(discussionPair._1.value); // throws if not correct format
        assertNotNull(discussionPair._2);
      });
    journalListener.until.completes();
    journalListener.confirmExpectedEntries(2, 10);
    assertEquals(2, journalListener.confirmedCount.get().intValue());
    final ForumStarted event0 = adapter().asSource(journalListener.allEntries.get(0));
    assertEquals(ForumStarted.class, event0.getClass());
    final DiscussionStarted event1 = discussionAdapter().asSource(journalListener.allEntries.get(1));
    assertEquals(DiscussionStarted.class, event1.getClass());
    assertEquals(author.value, event1.authorId);
    assertEquals(topic, event1.topic);
  }

  private EntryAdapterProvider adapter() {
    return registry.info(ForumEntity.class).entryAdapterProvider;
  }

  private EntryAdapterProvider discussionAdapter() {
    return registry.info(DiscussionEntity.class).entryAdapterProvider;
  }
}
