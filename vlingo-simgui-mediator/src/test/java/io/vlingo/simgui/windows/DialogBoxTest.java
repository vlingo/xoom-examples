// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.simgui.windows;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import io.vlingo.actors.testkit.AccessSafely;
import io.vlingo.simgui.Desktop;
import io.vlingo.simgui.geometry.Point;
import io.vlingo.simgui.geometry.Rectangle;
import io.vlingo.simgui.windows.DialogBox.CompletableReached;
import io.vlingo.simgui.windows.DialogBox.Completed;
import io.vlingo.simgui.windows.DialogBox.Completed.Result;
import io.vlingo.simgui.windows.Window.Enabled;
import io.vlingo.simgui.windows.mediator.SchemaSelectionDialog;

public class DialogBoxTest {

  @Test
  public void testThatSelectListItemCausesEvents() throws Exception {
    final MockParent parent = new MockParent();
    
    AccessSafely access = parent.afterCompleting(1);

    final Specification spec =
            DialogBox.with(
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

    final DialogBox dialog = Desktop.instance.windowFor(spec);

    access.readFrom("events");

    access.totalWritesGreaterThan(0, 500);
    final Enabled dialogEnabled = parent.event(0);
    assertEquals(Enabled.class, dialogEnabled.getClass());

    final Listbox list = dialog.childOf("list").await();
    list.select(3);

    access.totalWritesGreaterThan(1, 500);
    final CompletableReached completableReached = parent.event(1);
    assertEquals(CompletableReached.class, completableReached.getClass());

    final Button ok = dialog.childOf("ok").await();
    ok.click(new Point(1, 1));

    access.totalWritesGreaterThan(2, 500);
    final Completed<String> completed = parent.event(2);
    assertEquals(Completed.class, completed.getClass());
    assertEquals(Result.SUCCEEDED, completed.result);
    assertEquals("CartCheckedOut", completed.value);
  }
}
