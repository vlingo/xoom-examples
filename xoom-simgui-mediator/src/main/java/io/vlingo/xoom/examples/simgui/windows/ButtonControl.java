// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.simgui.windows;

import io.vlingo.xoom.examples.simgui.geometry.Point;

public class ButtonControl extends WindowComponent implements Button {
  private final Button self;

  public ButtonControl() {
    this.self = selfAs(Button.class);
  }

  @Override
  public void click(final Point point) {
    final WindowInfo info = selfWindowInfo();
    if (isEnabled()) {
      info.parent.on(new Clicked(self, info.id, info.tag, info.title, point));
    }
  }
}
