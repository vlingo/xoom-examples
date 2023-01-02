// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.simgui.windows;

public abstract class Event {
  public final String content;
  public final Id id;
  public final String tag;
  public final String type;
  public final Window window;

  public boolean is(final Class<?> type) {
    return getClass() == type;
  }

  @SuppressWarnings("unchecked")
  public <T> T typed() {
    return (T) this;
  }

  @Override
  public int hashCode() {
    return 31 * id.hashCode() * tag.hashCode() * content.hashCode();
  }

  @Override
  public boolean equals(final Object other) {
    if (other == null || other.getClass() != getClass()) {
      return false;
    }
    final Event otherEvent = (Event) other;
    return this.id.equals(otherEvent.id) && this.tag.equals(otherEvent.tag) && this.content.equals(otherEvent.content);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "[id=" + id + " tag=" + tag + " content=" + content + "]";
  }

  protected Event(final Window window, final Id id, final String tag, final String content) {
    this.window = window;
    this.id = id;
    this.tag = tag;
    this.type = getClass().getSimpleName();
    this.content = content;
  }
}
