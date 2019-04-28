package io.vlingo.simgui.windows;

public interface ParentWindow {
  <W extends Window> W createChild(final Specification specification);
}
