// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
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

import io.vlingo.common.Tuple2;
import io.vlingo.symbio.EntryAdapterProvider;

public class PostTest extends EntityTest {
  private Fixtures fixtures = new Fixtures();

  @Test
  public void testThatPostModerated() {
    final Tuple2<PostId, Post> postPair = fixtures.postFixture(world, journalListener);
    assertEquals(3, journalListener.confirmExpectedEntries(3, 10));
    this.journalListener.until = until(1);
    final Moderator moderator = Moderator.unique();
    final String subject = "A Moderated Subject";
    final String bodyText = "A moderated body text document.";
    assertNotNull(postPair);
    assertNotNull(postPair._1);
    assertNotNull(postPair._2);
    postPair._2.moderate(moderator, subject, bodyText);
    journalListener.until.completes();
    assertEquals(4, journalListener.confirmExpectedEntries(4, 10));
    final PostModerated event3 = adapter().asSource(journalListener.allEntries.get(3));
    assertEquals(PostModerated.class, event3.getClass());
    assertEquals(moderator.value, event3.moderatorId);
    assertEquals(subject, event3.subject);
    assertEquals(bodyText, event3.bodyText);
  }

  private EntryAdapterProvider adapter() {
    return registry.info(PostEntity.class).entryAdapterProvider;
  }
}
