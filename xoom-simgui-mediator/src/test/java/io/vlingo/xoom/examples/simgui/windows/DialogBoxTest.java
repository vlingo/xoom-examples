// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.simgui.windows;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;

import org.junit.Test;

import io.vlingo.xoom.examples.simgui.Desktop;
import io.vlingo.xoom.examples.simgui.geometry.Point;
import io.vlingo.xoom.examples.simgui.geometry.Rectangle;
import io.vlingo.xoom.examples.simgui.windows.DialogBox.CompletableReached;
import io.vlingo.xoom.examples.simgui.windows.DialogBox.Completed;
import io.vlingo.xoom.examples.simgui.windows.DialogBox.Completed.Result;
import io.vlingo.xoom.examples.simgui.windows.Window.Enabled;
import io.vlingo.xoom.examples.simgui.windows.mediator.SchemaSelectionDialog;

public class DialogBoxTest {

  @Test
  public void testThatSelectListItemCausesEvents() throws Exception {
    final MockParent parent = new MockParent();

    final Specification spec = specification(parent);

    final DialogBox dialog = Desktop.instance.windowFor(spec);

    final Enabled dialogEnabled = parent.nextEvent();
    assertNotNull(dialogEnabled);

    final Listbox list = dialog.childOf("list").await();
    list.select(3);

    final CompletableReached completableReached = parent.nextEvent();
    assertNotNull(completableReached);

    final Button ok = dialog.childOf("ok").await();
    ok.click(new Point(1, 1));

    final Completed<String> completed = parent.nextEvent();
    assertNotNull(completed);
    assertEquals(Result.SUCCEEDED, completed.result);
    assertEquals("CartCheckedOut", completed.value);
  }

  private Specification specification(final MockParent parent) {
    return DialogBox.with(
            SchemaSelectionDialog.class,
            parent,
            "Select Schema",
            Rectangle.with(10, 10, 250, 250),
            "schema-dialog",
            Arrays.asList(
                    "CartItemAdded",
                    "CartItemRemoved",
                    "CartItemRegistered",
                    "CartCheckedOut"));
  }
}
