// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.simgui.windows;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import io.vlingo.simgui.Desktop;
import io.vlingo.simgui.geometry.Rectangle;
import io.vlingo.simgui.windows.Textbox.TextChanged;
import io.vlingo.simgui.windows.Window.Enabled;

public class TextboxTest {

  @Test
  public void testThatTextboxSendsEvent() {
    final MockParent parent = new MockParent();
    final Specification childSpec = Textbox.with(parent, "text", new Rectangle(), true);
    final Textbox textbox = Desktop.instance.windowFor(childSpec);
    assertNotNull(textbox);
    final Enabled enabled = parent.nextEvent();
    assertEquals("text", enabled.tag);

    final String content = "This is a text entry.";
    textbox.content(content);
    final TextChanged changed = parent.nextEvent();
    assertEquals(content, changed.content);
  }
}
