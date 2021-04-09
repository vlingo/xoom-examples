// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.simgui.geometry;

public class Rectangle {
  public final Point topLeft;
  public final Point bottomRight;

  public static Rectangle home() {
    return new Rectangle();
  }

  public static Rectangle with(final int topX, final int topY, final int bottomX, final int bottomY) {
    return new Rectangle(topX, topY, bottomX, bottomY);
  }

  public static Rectangle with(final Point topLeft, final Point bottomRight) {
    return new Rectangle(topLeft, bottomRight);
  }

  public Rectangle(final int topX, final int topY, final int bottomX, final int bottomY) {
    this(new Point(topX, topY), new Point(bottomX, bottomY));
  }

  public Rectangle(final Point topLeft, final Point bottomRight) {
    this.topLeft = topLeft;
    this.bottomRight = bottomRight;
  }

  public Rectangle() {
    this(new Point(0, 0), new Point(0, 0));
  }

  @Override
  public int hashCode() {
    return 31 * topLeft.hashCode() * bottomRight.hashCode();
  }

  @Override
  public boolean equals(final Object other) {
    if (other == null || other.getClass() != Rectangle.class) {
      return false;
    }

    final Rectangle otherRectangle = (Rectangle) other;

    return topLeft.equals(otherRectangle.topLeft) && bottomRight.equals(otherRectangle.bottomRight);
  }

  @Override
  public String toString() {
    return "Rectangle[topLeft=" + topLeft + " bottomRight=" + bottomRight + "]";
  }
}
