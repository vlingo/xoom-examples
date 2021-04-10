// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.simgui.windows;

import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.examples.simgui.geometry.Rectangle;

public interface DialogBox extends Window {
  public static <T> Specification with(final Class<? extends DialogBoxComponent> type, final Window parent, final String title, final Rectangle rectangle,  final String tag, final Object...parameters) {
    return new Specification(DialogBox.class, type, parent, title, rectangle, true, tag, parameters);
  }

  public static <T> Specification with(final Class<? extends DialogBoxComponent> type, final Window parent, final String title, final Rectangle rectangle, final Object...parameters) {
    return new Specification(DialogBox.class, type, parent, title, rectangle, true, title, parameters);
  }

  public static <T> Specification with(final Class<? extends DialogBoxComponent> type, final Window parent, final String title, final Rectangle rectangle) {
    return new Specification(DialogBox.class, type, parent, title, rectangle, true, title);
  }

  void cancel();
  <T extends Window> Completes<T> childOf(final String tag);

  public static class Completed<T> extends Event {
    public enum Result { SUCCEEDED, CANCELLED };

    public final Result result;
    public final T value;

    public Completed(Window window, Id id, String tag, String content, final Result result, final T value) {
      super(window, id, tag, content);
      this.result = result;
      this.value = value;
    }
  }

  public static class CompletableReached extends Event {
    public CompletableReached(Window window, Id id, String tag) {
      super(window, id, tag, "");
    }
  }

  public static class CompletableWithdrawn extends Event {
    public CompletableWithdrawn(Window window, Id id, String tag) {
      super(window, id, tag, "");
    }
  }
}
