package io.vlingo.simgui.windows;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import io.vlingo.actors.testkit.AccessSafely;
import io.vlingo.common.Completes;

public class MockParent implements Window {
  private AccessSafely access;
  private List<Event> events = new CopyOnWriteArrayList<>();

  @Override
  public void createSelf(final Specification specification) {

  }

  @Override
  public void enable() {

  }

  @Override
  public void disable() {

  }

  @Override
  public void on(final Event event) {
    access.writeUsing("events", event);
  }

  @Override
  public Completes<WindowInfo> windowInfo() {
    return null;
  }

  public AccessSafely afterCompleting(final int happenings) {
    access = AccessSafely.afterCompleting(happenings);
    
    access
      .writingWith("events", (Event event) -> events.add(event))
      .readingWith("events", () -> events);
    
    return access;
  }

  @SuppressWarnings("unchecked")
  public <E extends Event> E event(final int index) {
    return (E) events.get(index);
  }

  public List<Event> events() {
    return access.readFrom("events");
  }

  public List<Event> events(final int retries, final List<Event> expected) {
    return access.readFromExpecting("events", expected, retries);
  }
}
