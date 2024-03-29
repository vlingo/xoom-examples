// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.saasovation.collaboration.model.forum;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.saasovation.collaboration.model.EntityTest;
import com.saasovation.collaboration.model.Moderator;
import com.saasovation.collaboration.model.forum.Events.PostModerated;

import io.vlingo.xoom.common.Tuple2;
import io.vlingo.xoom.symbio.EntryAdapterProvider;

public class PostTest extends EntityTest {
  private Fixtures fixtures = new Fixtures();

  @Test
  public void testThatPostModerated() {
    journalDispatcher.afterCompleting(3);
    final Tuple2<PostId, Post> postPair = fixtures.postFixture(world);
    final int count3 = journalDispatcher.confirmedCount(3);
    assertEquals(3, count3);
    journalDispatcher.afterCompleting(1);
    final Moderator moderator = Moderator.unique();
    final String subject = "A Moderated Subject";
    final String bodyText = "A moderated body text document.";
    assertNotNull(postPair);
    assertNotNull(postPair._1);
    assertNotNull(postPair._2);
    postPair._2.moderate(moderator, subject, bodyText);
    final int count4 = journalDispatcher.confirmedCount(4);
    assertEquals(4, count4);
    final PostModerated event3 = adapter().asSource(journalDispatcher.entry(3));
    assertEquals(PostModerated.class, event3.getClass());
    assertEquals(moderator.value, event3.moderatorId);
    assertEquals(subject, event3.subject);
    assertEquals(bodyText, event3.bodyText);
  }

  private EntryAdapterProvider adapter() {
    return registry.info(PostEntity.class).entryAdapterProvider;
  }
}
