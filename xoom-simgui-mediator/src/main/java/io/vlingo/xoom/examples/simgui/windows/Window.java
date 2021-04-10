// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.simgui.windows;

import io.vlingo.xoom.common.Completes;

public interface Window {
  public static Window NoWindow = new NoWindow();

  @SuppressWarnings("unchecked")
  public static <T> T typed(final Window window) {
    return (T) window;
  }

  void createSelf(final Specification specification);

  void enable();

  void disable();

  void on(final Event event);

  Completes<WindowInfo> windowInfo();

  public static final class Enabled extends Event {
    Enabled(final Window window, final Id id, final String tag) {
      super(window, id, tag, "");
    }
  }

  public static final class Disabled extends Event {
    Disabled(final Window window, final Id id, final String tag) {
      super(window, id, tag, "");
    }
  }
}
