// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.simgui.windows;

import java.util.ArrayList;
import java.util.List;

import io.vlingo.common.Completes;

public class ListboxControl extends WindowComponent implements Listbox {
  private List<String> items;
  private int selection;
  private final Listbox self;

  public ListboxControl() {
    this.self = selfAs(Listbox.class);
    this.selection = -1;
  }

  @Override
  public void items(final List<String> content) {
    items = content == null ? new ArrayList<>(0) : content;
  }

  @Override
  public Completes<Integer> selection() {
    return completes().with(selection);
  }

  @Override
  public Completes<String> selectedItem() {
    final String item =
            (selection > 0 && selection < items.size()) ?
                    items.get(selection) :
                    "";

    return completes().with(item);
  }

  @Override
  public void select(final int itemIndex) {
    if (itemIndex == selection) {
      return;
    }
    if (itemIndex >= 0 && itemIndex < items.size()) {
      selection = itemIndex;
      final WindowInfo info = selfWindowInfo();
      info.parent.on(new ItemSelected(self, info.id, info.tag, items.get(selection), itemIndex));
    } else if (itemIndex == -1) {
      selection = itemIndex;
      final WindowInfo info = selfWindowInfo();
      info.parent.on(new ItemDeselected(self, info.id, info.tag));
    }
  }

  @Override
  public void select(final String item) {
    for (int index = 0; index < items.size(); ++index) {
      if (items.get(index).equals(item)) {
        select(index);
        return;
      }
    }
  }
}
