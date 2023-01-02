// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.simgui.windows;

import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.examples.simgui.geometry.Rectangle;

public interface Textbox extends Window {
  public static <T> Specification with(final Window parent, final String title, final Rectangle rectangle, final boolean enabled, final String tag, final Object...parameters) {
    return new Specification(Textbox.class, TextboxControl.class, parent, title, rectangle, enabled, tag, parameters);
  }

  public static <T> Specification with(final Window parent, final String title, final Rectangle rectangle, final boolean enabled, final Object...parameters) {
    return new Specification(Textbox.class, TextboxControl.class, parent, title, rectangle, enabled, title, parameters);
  }

  public static <T> Specification with(final Window parent, final String title, final Rectangle rectangle, final boolean enabled) {
    return new Specification(Textbox.class, TextboxControl.class, parent, title, rectangle, enabled, title);
  }

  void content(final String text);
  
  Completes<String> content();

  public static final class TextChanged extends Event {
    TextChanged(final Textbox window, final Id id, final String tag, final String content) {
      super(window, id, tag, content);
    }
  }
}
