// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.simgui.windows;

import io.vlingo.xoom.common.Completes;

public class TextboxControl extends WindowComponent implements Textbox {
  private String content;
  private Textbox self;

  public TextboxControl() {
    this.content = "";
    this.self = selfAs(Textbox.class);
  }

  @Override
  public Completes<String> content() {
    return completes().with(content);
  }

  @Override
  public void content(final String text) {
    if (!content.equals(text)) {
      content = text;
      final WindowInfo info = selfWindowInfo();
      info.parent.on(new TextChanged(self, info.id, info.tag, content));
    }
  }
}
