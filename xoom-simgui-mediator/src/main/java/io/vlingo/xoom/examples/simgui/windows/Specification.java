// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.simgui.windows;

import java.util.Arrays;
import java.util.List;

import io.vlingo.xoom.actors.Actor;
import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.examples.simgui.geometry.Rectangle;

public class Specification {
  public final Definition definition;
  public final boolean enabled;
  public final List<Object> parameters;
  public final Class<?> protocol;
  public final Rectangle rectangle;
  public final WindowInfo windowInfo;

  public Specification(
          final Class<?> protocol,
          final Class<? extends Actor> type,
          final Window parent,
          final String title,
          final Rectangle rectangle,
          final boolean enabled,
          String tag,
          final Object...parameters) {

    this.protocol = protocol;
    this.rectangle = rectangle;
    this.windowInfo = new WindowInfo(parent, Id.unique(), tag, title);
    this.enabled = enabled;
    this.parameters = Arrays.asList(parameters);
    this.definition = Definition.has(type, Definition.NoParameters);
  }

  public boolean hasParameters() {
    return !parameters.isEmpty();
  }

  @SuppressWarnings("unchecked")
  public <T> T parameter(final int index) {
    if (index >= 0 && index < parameters.size()) {
      return (T) parameters.get(index);
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  public <T> T parameters() {
    return (T) parameters;
  }
}
