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
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import io.vlingo.actors.testkit.AccessSafely;
import io.vlingo.simgui.Desktop;
import io.vlingo.simgui.geometry.Point;
import io.vlingo.simgui.geometry.Rectangle;
import io.vlingo.simgui.windows.Button.Clicked;
import io.vlingo.simgui.windows.Window.Enabled;

public class ButtonTest {

  @Test
  public void testThatButtonSendsEvent() {
    final MockParent parent = new MockParent();
    final AccessSafely access = parent.afterCompleting(1);
    final Specification childSpec = Button.with(parent, "Ok", new Rectangle(), true);
    final Button button = Desktop.instance.windowFor(childSpec);
    assertNotNull(button);
    access.totalWritesGreaterThan(0, 500);
    final Point point = new Point(0, 0);
    button.click(point);
    access.totalWritesGreaterThan(1, 500);
    final List<Event> events = parent.events();
    assertNotNull(events);
    assertFalse(events.isEmpty());
    final Button.Clicked clicked = events.get(1).typed();
    assertEquals(point, clicked.point);
  }

  @Test(expected = IllegalStateException.class)
  public void testThatDisabledButtonClickFails() {
    final MockParent parent = new MockParent();
    parent.afterCompleting(1);
    final Specification childSpec = Button.with(parent, "Ok", new Rectangle(), true);
    final Button button = Desktop.instance.windowFor(childSpec);
    assertNotNull(button);
    final Enabled enabled = new Enabled(button, childSpec.windowInfo.id, childSpec.windowInfo.tag);
    List<Event> events = parent.events(500, Arrays.asList(enabled));
    button.disable();
    final Point point = new Point(0, 0);
    button.click(point);
    final Clicked clicked = new Clicked(button, childSpec.windowInfo.id, childSpec.windowInfo.tag, "Ok", point);
    events = parent.events(500, Arrays.asList(clicked)); // should fail here
    assertNotNull(events);
    assertTrue(events.isEmpty());
  }
}
