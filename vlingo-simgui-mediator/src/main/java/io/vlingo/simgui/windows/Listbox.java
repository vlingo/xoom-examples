// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.simgui.windows;

import java.util.List;

import io.vlingo.common.Completes;
import io.vlingo.simgui.geometry.Rectangle;

public interface Listbox extends Window {
  public static <T> Specification with(final Window parent, final String title, final Rectangle rectangle, final boolean enabled, final String tag, final Object...parameters) {
    return new Specification(Listbox.class, ListboxControl.class, parent, title, rectangle, enabled, tag, parameters);
  }

  public static <T> Specification with(final Window parent, final String title, final Rectangle rectangle, final boolean enabled, final Object...parameters) {
    return new Specification(Listbox.class, ListboxControl.class, parent, title, rectangle, enabled, title, parameters);
  }

  public static <T> Specification with(final Window parent, final String title, final Rectangle rectangle, final boolean enabled) {
    return new Specification(Listbox.class, ListboxControl.class, parent, title, rectangle, enabled, title);
  }

  void items(final List<String> content);

  Completes<Integer> selection();
  Completes<String> selectedItem();
  void select(final int itemIndex);
  void select(final String item);

  public static final class ItemSelected extends Event {
    public final int itemIndex;
    ItemSelected(final Listbox window, final Id id, final String tag, final String content, final int itemIndex) {
      super(window, id, tag, content);
      this.itemIndex = itemIndex;
    }
  }

  public static final class ItemDeselected extends Event {
    ItemDeselected(final Listbox window, final Id id, final String tag) {
      super(window, id, tag, "");
    }
  }
}
