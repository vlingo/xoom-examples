// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.simgui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import io.vlingo.xoom.examples.simgui.geometry.Point;
import io.vlingo.xoom.examples.simgui.geometry.Rectangle;
import io.vlingo.xoom.examples.simgui.windows.Button;
import io.vlingo.xoom.examples.simgui.windows.MockParent;
import io.vlingo.xoom.examples.simgui.windows.Specification;

public class DesktopTest {

  @Test
  public void testThatDesktopInitializes() {
    assertNotNull(Desktop.instance);
  }

  @Test
  public void testThatDesktopCreatesWindow() {
    final MockParent parent = new MockParent();
    final Specification childSpec = Button.with(parent, "Ok", new Rectangle(), true);
    final Button button = Desktop.instance.windowFor(childSpec);
    assertNotNull(button);
    parent.nextEvent(); // throw out Enabled
    button.click();
    final Button.Clicked clicked = parent.nextEvent();
    assertEquals(Point.home(), clicked.point);
  }
}
