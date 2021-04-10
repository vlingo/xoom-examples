// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.simgui.windows.mediator;

import io.vlingo.xoom.examples.simgui.geometry.Rectangle;
import io.vlingo.xoom.examples.simgui.windows.Button;
import io.vlingo.xoom.examples.simgui.windows.Button.Clicked;
import io.vlingo.xoom.examples.simgui.windows.DialogBox;
import io.vlingo.xoom.examples.simgui.windows.DialogBox.Completed.Result;
import io.vlingo.xoom.examples.simgui.windows.DialogBoxComponent;
import io.vlingo.xoom.examples.simgui.windows.Event;
import io.vlingo.xoom.examples.simgui.windows.Listbox;
import io.vlingo.xoom.examples.simgui.windows.Listbox.ItemSelected;
import io.vlingo.xoom.examples.simgui.windows.Specification;
import io.vlingo.xoom.examples.simgui.windows.Textbox;
import io.vlingo.xoom.examples.simgui.windows.Textbox.TextChanged;
import io.vlingo.xoom.examples.simgui.windows.WindowInfo;

public class SchemaSelectionDialog extends DialogBoxComponent {
  private Textbox text;
  private Listbox list;
  private Button ok;
  private Button cancel;

  private boolean okEnabled;
  private String value;

  private DialogBox self;

  public SchemaSelectionDialog() {
    this.self = selfAs(DialogBox.class);
    this.okEnabled = false;
  }

  @Override
  public void cancel() {
    cancel.click();
  }

  @Override
  public void createSelf(final Specification specification) {
    super.createSelf(specification);

    text = createChild(Textbox.with(self, "text", new Rectangle(10, 10, 200, 20), true, "text"));
    list = createChild(Listbox.with(self, "list", new Rectangle(10, 30, 200, 200), true, "list"));
    ok = createChild(Button.with(self, "Ok", new Rectangle(120, 250, 140, 270), false, "ok"));
    cancel = createChild(Button.with(self, "Cancel", new Rectangle(160, 250, 200, 270), true, "cancel"));

    if (specification.hasParameters()) {
      list.items(specification.parameter(0));
    }

    ok.disable();
  }

  @Override
  public void on(final Event event) {
    final WindowInfo info = selfWindowInfo();

    switch (event.type) {
    case "Enabled":
      if (event.tag.equals("ok")) {
        if (!okEnabled) {
          okEnabled = true;
          info.parent.on(new CompletableReached(self, info.id, info.tag));
        }
      }
      break;
    case "Disabled":
      if (event.tag.equals("ok")) {
        if (okEnabled) {
          okEnabled = false;
          info.parent.on(new CompletableWithdrawn(self, info.id, info.tag));
        }
      }
      break;
    case "ItemSelected":
      handleList(event.typed());
      break;
    case "TextChanged":
      handleText(event.typed());
      break;
    case "Clicked":
      handleButton(event.typed());
      break;
    default:
      break;
    }
  }

  private void handleText(final TextChanged changed) {
    if (!changed.content.isEmpty()) {
      list.select(changed.content);
      value = changed.content;
      ok.enable();
    } else {
      list.select(-1);
      ok.disable();
    }
  }

  private void handleList(final ItemSelected selected) {
    text.content(selected.content);
  }

  private void handleButton(final Clicked clicked) {
    final Result result = clicked.tag.equals("ok") ? Result.SUCCEEDED : Result.CANCELLED;
    final WindowInfo info = selfWindowInfo();
    info.parent.on(new Completed<String>(self, info.id, info.tag, clicked.content, result, value));
  }
}
