package io.vlingo.xoom.examples.petclinic.model.client;

public class Visit {

  public final long start;
  public final long end;
  public final String description;

  public static Visit of(final long start, final long end, final String description) {
    return new Visit(start, end, description);
  }

  private Visit (final long start, final long end, final String description) {
    this.start = start;
    this.end = end;
    this.description = description;
  }

}