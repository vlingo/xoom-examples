// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.simgui.windows;

import java.util.HashMap;
import java.util.Map;

import io.vlingo.common.Completes;

public abstract class DialogBoxComponent extends WindowComponent implements DialogBox, ParentWindow {
  private final Map<String,Window> children = new HashMap<>();

  @Override
  @SuppressWarnings("unchecked")
  public <T extends Window> Completes<T> childOf(final String tag) {
    for (final String childTag : children.keySet()) {
      if (childTag.equals(tag)) {
        final Window child = children.get(childTag);
        return completes().with((T) child);
      }
    }
    return completes().with((T) NoWindow);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <W extends Window> W createChild(final Specification specification) {
    final W child = (W) childActorFor(specification.protocol, specification.definition);
    children.put(specification.windowInfo.tag, child);
    child.createSelf(specification);
    return child;
  }

  protected DialogBoxComponent() { }
}
