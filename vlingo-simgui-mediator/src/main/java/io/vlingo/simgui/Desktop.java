// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.simgui;

import io.vlingo.actors.World;
import io.vlingo.simgui.windows.Specification;
import io.vlingo.simgui.windows.Window;

public class Desktop {
  public static final Desktop instance;

  private final World world;

  static {
    instance = new Desktop();
  }

  @SuppressWarnings("unchecked")
  public <T extends Window> T windowFor(final Specification spec) {
    final T window = (T) world.actorFor(spec.protocol, spec.definition);
    window.createSelf(spec);
    return window;
  }

  private Desktop() {
    this.world = World.startWithDefaults("simgui");
  }
}
