// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.simgui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import io.vlingo.simgui.geometry.Point;
import io.vlingo.simgui.geometry.Rectangle;
import io.vlingo.simgui.windows.Button;
import io.vlingo.simgui.windows.Event;
import io.vlingo.simgui.windows.MockParent;
import io.vlingo.simgui.windows.Specification;

public class DesktopTest {

  @Test
  public void testThatDesktopInitializes() {
    assertNotNull(Desktop.instance);
  }

  @Test
  public void testThatDesktopCreatesWindow() {
    final MockParent parent = new MockParent();
    parent.afterCompleting(1);
    final Specification childSpec = Button.with(parent, "Ok", new Rectangle(), true);
    final Button button = Desktop.instance.windowFor(childSpec);
    assertNotNull(button);
    List<Event> events = parent.events();
    parent.afterCompleting(1);
    final Point point = new Point(0, 0);
    button.click(point);
    events = parent.events();
    assertNotNull(events);
    assertFalse(events.isEmpty());
    final Button.Clicked clicked = events.get(1).typed();
    assertEquals(point, clicked.point);
  }
}
