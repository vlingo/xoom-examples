// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.simgui.windows;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import io.vlingo.xoom.examples.simgui.Desktop;
import io.vlingo.xoom.examples.simgui.geometry.Rectangle;
import io.vlingo.xoom.examples.simgui.windows.Listbox.ItemDeselected;
import io.vlingo.xoom.examples.simgui.windows.Listbox.ItemSelected;
import io.vlingo.xoom.examples.simgui.windows.Window.Enabled;

public class ListboxTest {

  @Test
  public void testThatListboxSendsItemSelectedByIndexEvent() {
    final MockParent parent = new MockParent();
    final Specification childSpec = Listbox.with(parent, "list", new Rectangle(), true);
    final Listbox listbox = Desktop.instance.windowFor(childSpec);
    assertNotNull(listbox);
    final Enabled enabled = parent.nextEvent();
    assertEquals("list", enabled.tag);

    final List<String> content = Arrays.asList("One", "Two", "Three");
    listbox.items(content);
    listbox.select(1);
    final ItemSelected selected = parent.nextEvent();
    assertEquals(1, selected.itemIndex);
    assertEquals(content.get(1), selected.content);
  }

  @Test
  public void testThatListboxSendsItemSelectedByTextEvent() {
    final MockParent parent = new MockParent();
    final Specification childSpec = Listbox.with(parent, "list", new Rectangle(), true);
    final Listbox listbox = Desktop.instance.windowFor(childSpec);
    assertNotNull(listbox);
    final Enabled enabled = parent.nextEvent();
    assertEquals("list", enabled.tag);
    
    final List<String> content = Arrays.asList("One", "Two", "Three");
    listbox.items(content);
    listbox.select("Three");
    final ItemSelected selected = parent.nextEvent();
    assertEquals(2, selected.itemIndex);
    assertEquals(content.get(2), selected.content);
  }

  @Test
  public void testThatListboxSendsDeselected() {
    final MockParent parent = new MockParent();
    final Specification childSpec = Listbox.with(parent, "list", new Rectangle(), true);
    final Listbox listbox = Desktop.instance.windowFor(childSpec);
    assertNotNull(listbox);
    final Enabled enabled = parent.nextEvent();
    assertEquals("list", enabled.tag);

    final List<String> content = Arrays.asList("One", "Two", "Three");
    listbox.items(content);
    listbox.select("Three");
    final ItemSelected selected = parent.nextEvent();
    assertEquals(2, selected.itemIndex);
    assertEquals(content.get(2), selected.content);

    listbox.select(-1);
    final ItemDeselected deselected = parent.nextEvent();
    assertTrue(deselected.content.isEmpty());
  }
}
