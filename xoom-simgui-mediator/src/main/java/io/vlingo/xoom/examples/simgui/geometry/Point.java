// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.simgui.geometry;

public class Point {
  public final int x;
  public final int y;

  public static Point home() {
    return new Point();
  }

  public static Point with(final int x, final int y) {
    return new Point(x, y);
  }

  public Point(final int x, final int y) {
    this.x = x;
    this.y = y;
  }

  public Point() {
    this(0, 0);
  }

  @Override
  public int hashCode() {
    return 31 * x * y;
  }

  @Override
  public boolean equals(final Object other) {
    if (other == null || other.getClass() != Point.class) {
      return false;
    }

    final Point otherPoint = (Point) other;

    return x == otherPoint.x && y == otherPoint.y;
  }

  @Override
  public String toString() {
    return "Point[x=" + x + " y=" + y + "]";
  }
}
