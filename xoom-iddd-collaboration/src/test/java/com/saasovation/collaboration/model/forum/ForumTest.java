// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.saasovation.collaboration.model.forum;

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
import io.vlingo.xoom.common.Tuple2;
import io.vlingo.xoom.symbio.EntryAdapterProvider;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ForumTest extends EntityTest {
  private Fixtures fixtures = new Fixtures();

  @Test
  public void testThatForumStarted() {
    journalDispatcher.afterCompleting(1);
    final ForumDescription description = fixtures.forumDescriptionFixture();
    final Tuple2<ForumId,Forum> forumPair = Forum.startWith(world.stage(), Tenant.unique(), description);
    assertNotNull(forumPair);
    assertNotNull(forumPair._1);
    UUID.fromString(forumPair._1.value); // throws if not correct format
    assertNotNull(forumPair._2);
    final int count = journalDispatcher.confirmedCount();
    assertEquals(1, count);
    final ForumStarted event = adapter().asSource(journalDispatcher.entry(0));
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
    journalDispatcher.afterCompleting(2);
    final Tuple2<ForumId,Forum> forumPair = Forum.startWith(world.stage(), Tenant.unique(), fixtures.forumDescriptionFixture());
    final Moderator moderator = Moderator.unique();
    forumPair._2.assign(moderator);
    final int count = journalDispatcher.confirmedCount();
    assertEquals(2, count);
    final ForumStarted event0 = adapter().asSource(journalDispatcher.entry(0));
    assertEquals(ForumStarted.class, event0.getClass());
    final ForumModeratorAssigned event1 = adapter().asSource(journalDispatcher.entry(1));
    assertEquals(ForumModeratorAssigned.class, event1.getClass());
    assertEquals(moderator.value, event1.moderatorId);
  }

  @Test
  public void testThatForumClosed() {
    journalDispatcher.afterCompleting(2);
    final Tuple2<ForumId,Forum> forumPair = Forum.startWith(world.stage(), Tenant.unique(), fixtures.forumDescriptionFixture());
    forumPair._2.close();
    final int count = journalDispatcher.confirmedCount();
    assertEquals(2, count);
    final ForumStarted event0 = adapter().asSource(journalDispatcher.entry(0));
    assertEquals(ForumStarted.class, event0.getClass());
    final ForumClosed event1 = adapter().asSource(journalDispatcher.entry(1));
    assertEquals(ForumClosed.class, event1.getClass());
  }

  @Test
  public void testThatForumDescribed() {
    journalDispatcher.afterCompleting(2);
    final Tuple2<ForumId,Forum> forumPair = Forum.startWith(world.stage(), Tenant.unique(), fixtures.forumDescriptionFixture());
    final String description = "Let's discuss the vlingo/platform";
    forumPair._2.describeAs(description);
    final int count = journalDispatcher.confirmedCount();
    assertEquals(2, count);
    final ForumStarted event0 = adapter().asSource(journalDispatcher.entry(0));
    assertEquals(ForumStarted.class, event0.getClass());
    final ForumDescribed event1 = adapter().asSource(journalDispatcher.entry(1));
    assertEquals(ForumDescribed.class, event1.getClass());
    assertEquals(description, event1.description);
  }

  @Test
  public void testThatForumReopened() {
    journalDispatcher.afterCompleting(1);
    final Tuple2<ForumId,Forum> forumPair = Forum.startWith(world.stage(), Tenant.unique(), fixtures.forumDescriptionFixture());
    final int count = journalDispatcher.confirmedCount();
    assertEquals(1, count);
    journalDispatcher.afterCompleting(1);
    forumPair._2.close();
    final int count2 = journalDispatcher.confirmedCount();
    assertEquals(2, count2);
    journalDispatcher.afterCompleting(1);
    forumPair._2.reopen();
    final int count3 = journalDispatcher.confirmedCount();
    assertEquals(3, count3);
    final ForumStarted event0 = adapter().asSource(journalDispatcher.entry(0));
    assertEquals(ForumStarted.class, event0.getClass());
    final ForumClosed event1 = adapter().asSource(journalDispatcher.entry(1));
    assertEquals(ForumClosed.class, event1.getClass());
    final ForumReopened event2 = adapter().asSource(journalDispatcher.entry(2));
    assertEquals(ForumReopened.class, event2.getClass());
  }

  @Test
  public void testThatForumTopicChanged() {
    journalDispatcher.afterCompleting(2);
    final Tuple2<ForumId,Forum> forumPair = Forum.startWith(world.stage(), Tenant.unique(), fixtures.forumDescriptionFixture());
    final String topic = "Even More Than All Things vlingo/platform";
    forumPair._2.topicIs(topic);
    final int count = journalDispatcher.confirmedCount();
    assertEquals(2, count);
    final ForumStarted event0 = adapter().asSource(journalDispatcher.entry(0));
    assertEquals(ForumStarted.class, event0.getClass());
    final ForumTopicChanged event1 = adapter().asSource(journalDispatcher.entry(1));
    assertEquals(ForumTopicChanged.class, event1.getClass());
    assertEquals(topic, event1.topic);
  }

  @Test
  public void testThatForumStartsDiscussion() {
    journalDispatcher.afterCompleting(2);
    final Tuple2<ForumId,Forum> forumPair = Forum.startWith(world.stage(), Tenant.unique(), fixtures.forumDescriptionFixture());
    final Author author = Author.unique();
    final String topic = "At Topic That Is of Great Interest";
    final String ownerId = UUID.randomUUID().toString();
    forumPair._2.discussFor(author, topic, ownerId)
      .andThenConsume(discussionPair -> {
        assertNotNull(discussionPair);
        assertNotNull(discussionPair._1);
        UUID.fromString(discussionPair._1.value); // throws if not correct format
        assertNotNull(discussionPair._2);
      });
    final int count = journalDispatcher.confirmedCount();
    assertEquals(2, count);
    final ForumStarted event0 = adapter().asSource(journalDispatcher.entry(0));
    assertEquals(ForumStarted.class, event0.getClass());
    final DiscussionStarted event1 = discussionAdapter().asSource(journalDispatcher.entry(1));
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
