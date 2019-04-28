// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.simgui.windows;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import io.vlingo.simgui.Desktop;
import io.vlingo.simgui.geometry.Rectangle;

public class ListboxTest {

  @Test
  public void testThatListboxSendsItemSelectedByIndexEvent() {
    final MockParent parent = new MockParent();
    parent.afterCompleting(1);
    final Specification childSpec = Listbox.with(parent, "list", new Rectangle(), true);
    final Listbox listbox = Desktop.instance.windowFor(childSpec);
    assertNotNull(listbox);
    List<Event> events = parent.events();
    final Window.Enabled enabled = events.get(0).typed();
    assertEquals("list", enabled.tag);
    
    parent.afterCompleting(1);
    final List<String> content = Arrays.asList("One", "Two", "Three");
    listbox.items(content);
    listbox.select(1);
    events = parent.events();
    final Listbox.ItemSelected selected = events.get(1).typed();
    assertEquals(1, selected.itemIndex);
    assertEquals(content.get(1), selected.content);
  }

  @Test
  public void testThatListboxSendsItemSelectedByTextEvent() {
    final MockParent parent = new MockParent();
    parent.afterCompleting(1);
    final Specification childSpec = Listbox.with(parent, "list", new Rectangle(), true);
    final Listbox listbox = Desktop.instance.windowFor(childSpec);
    assertNotNull(listbox);
    List<Event> events = parent.events();
    final Window.Enabled enabled = events.get(0).typed();
    assertEquals("list", enabled.tag);
    
    parent.afterCompleting(1);
    final List<String> content = Arrays.asList("One", "Two", "Three");
    listbox.items(content);
    listbox.select("Three");
    events = parent.events();
    final Listbox.ItemSelected selected = events.get(1).typed();
    assertEquals(2, selected.itemIndex);
    assertEquals(content.get(2), selected.content);
  }

  @Test
  public void testThatListboxSendsDeselected() {
    final MockParent parent = new MockParent();
    parent.afterCompleting(1);
    final Specification childSpec = Listbox.with(parent, "list", new Rectangle(), true);
    final Listbox listbox = Desktop.instance.windowFor(childSpec);
    assertNotNull(listbox);
    List<Event> events = parent.events();
    assertEquals(1, events.size());
    final Window.Enabled enabled = events.get(0).typed();
    assertEquals("list", enabled.tag);

    parent.afterCompleting(2);
    final List<String> content = Arrays.asList("One", "Two", "Three");
    listbox.items(content);
    listbox.select("Three");
    listbox.select(-1); // deselect
    events = parent.events();
    assertEquals(3, events.size());
    final Listbox.ItemSelected selected = events.get(1).typed();
    assertEquals(2, selected.itemIndex);
    assertEquals(content.get(2), selected.content);

    assertEquals(Listbox.ItemDeselected.class, events.get(2).typed().getClass());
    final Listbox.ItemDeselected deselected = events.get(2).typed();
    assertTrue(deselected.content.isEmpty());
  }
}
