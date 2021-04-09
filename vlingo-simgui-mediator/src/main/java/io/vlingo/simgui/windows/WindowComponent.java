// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.simgui.windows;

import io.vlingo.actors.Actor;
import io.vlingo.common.Completes;

public abstract class WindowComponent extends Actor implements Window {
  private boolean enabled;
  private final Window self;
  private WindowInfo windowInfo;

  public WindowComponent() {
    this.self = selfAs(Window.class);
  }

  @Override
  public void createSelf(final Specification specification) {
    windowInfo = specification.windowInfo;

    if (specification.enabled) {
      self.enable();
    } else {
      self.disable();
    }
  }

  @Override
  public void enable() {
    enabled = true;
    final Event event = new Enabled(self, windowInfo.id, windowInfo.tag);
    self.on(event);
    windowInfo.parent.on(event);
  }

  @Override
  public void disable() {
    this.enabled = false;
    final Event event = new Disabled(self, windowInfo.id, windowInfo.tag);
    self.on(event);
    windowInfo.parent.on(event);
  }

  @Override
  public void on(final Event event) {
    // override for behavior
  }

  @Override
  public Completes<WindowInfo> windowInfo() {
    return completes().with(windowInfo);
  }

  protected boolean isEnabled() {
    return enabled;
  }

  protected WindowInfo selfWindowInfo() {
    return windowInfo;
  }

  protected void selfWindowInfo(final WindowInfo windowInfo) {
    this.windowInfo = windowInfo;
  }
}
