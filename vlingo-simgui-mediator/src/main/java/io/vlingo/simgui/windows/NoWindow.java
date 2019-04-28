// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.simgui.windows;

import io.vlingo.common.Completes;

class NoWindow implements Window {
  @Override public void createSelf(final Specification specification) { }
  @Override public void enable() { }
  @Override public void disable() { }
  @Override public void on(Event event) { }
  @Override public Completes<WindowInfo> windowInfo() { return null; }
}
