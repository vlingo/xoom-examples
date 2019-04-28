// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.simgui.windows;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import io.vlingo.simgui.Desktop;
import io.vlingo.simgui.geometry.Rectangle;

public class TextboxTest {

  @Test
  public void testThatTextboxSendsEvent() {
    final MockParent parent = new MockParent();
    parent.afterCompleting(1);
    final Specification childSpec = Textbox.with(parent, "", new Rectangle(), true);
    final Textbox textbox = Desktop.instance.windowFor(childSpec);
    assertNotNull(textbox);
    List<Event> events = parent.events();
    parent.afterCompleting(1);
    final String content = "This is a text entry.";
    textbox.content(content);
    events = parent.events();
    assertNotNull(events);
    assertFalse(events.isEmpty());
    final Textbox.TextChanged changed = events.get(1).typed();
    assertEquals(content, changed.content);
  }
}
