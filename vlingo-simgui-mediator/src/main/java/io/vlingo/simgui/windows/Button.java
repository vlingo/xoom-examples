// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.simgui.windows;

import io.vlingo.simgui.geometry.Point;
import io.vlingo.simgui.geometry.Rectangle;

public interface Button extends Window {
  public static <T> Specification with(final Window parent, final String title, final Rectangle rectangle, final boolean enabled, final String tag, final Object...parameters) {
    return new Specification(Button.class, ButtonControl.class, parent, title, rectangle, enabled, tag, parameters);
  }

  public static <T> Specification with(final Window parent, final String title, final Rectangle rectangle, final boolean enabled, final Object...parameters) {
    return new Specification(Button.class, ButtonControl.class, parent, title, rectangle, enabled, title, parameters);
  }

  public static <T> Specification with(final Window parent, final String title, final Rectangle rectangle, final boolean enabled) {
    return new Specification(Button.class, ButtonControl.class, parent, title, rectangle, enabled, title);
  }

  void click(final Point point);
  default void click() { click(new Point(0, 0)); }

  public static final class Clicked extends Event {
    public final Point point;

    Clicked(final Button window, final Id id, final String tag, final String content, final Point point) {
      super(window, id, tag, content);

      this.point = point;
    }
  }
}
