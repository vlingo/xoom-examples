// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.simgui.windows;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import io.vlingo.xoom.examples.simgui.Desktop;
import io.vlingo.xoom.examples.simgui.geometry.Point;
import io.vlingo.xoom.examples.simgui.geometry.Rectangle;
import io.vlingo.xoom.examples.simgui.windows.Button.Clicked;
import io.vlingo.xoom.examples.simgui.windows.Window.Disabled;
import io.vlingo.xoom.examples.simgui.windows.Window.Enabled;

public class ButtonTest {

  @Test
  public void testThatButtonSendsEvent() {
    final MockParent parent = new MockParent();
    final Specification childSpec = Button.with(parent, "Ok", new Rectangle(), true);
    final Button button = Desktop.instance.windowFor(childSpec);
    assertNotNull(button);
    final Enabled enabled = parent.nextEvent();
    assertNotNull(enabled);
    final Point point = new Point(0, 0);
    button.click(point);
    final List<Event> events = parent.nextEvents();
    assertNotNull(events);
    assertFalse(events.isEmpty());
    final Button.Clicked clicked = events.get(1).typed();
    assertEquals(point, clicked.point);
  }

  @Test(expected = IllegalStateException.class)
  public void testThatDisabledButtonClickFails() {
    final MockParent parent = new MockParent();
    final Specification childSpec = Button.with(parent, "Ok", new Rectangle(), true);
    final Button button = Desktop.instance.windowFor(childSpec);
    assertNotNull(button);
    final Enabled enabled = parent.nextEvent();
    assertNotNull(enabled);
    button.disable();
    button.click();
    final Disabled disabled = parent.nextEvent();
    assertNotNull(disabled);
    final Clicked clicked = parent.nextEvent(); // should fail here
    assertNotNull(clicked);
  }
}
