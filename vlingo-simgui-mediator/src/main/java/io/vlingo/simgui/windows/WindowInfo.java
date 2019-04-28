// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.simgui.windows;

public class WindowInfo {
  public final Id id;
  public final Window parent;
  public final String tag;
  public final String title;

  public WindowInfo(final Window parent, final Id id, final String tag, final String title) {
    this.parent = parent;
    this.id = id;
    this.tag = tag;
    this.title = title;
  }
}
