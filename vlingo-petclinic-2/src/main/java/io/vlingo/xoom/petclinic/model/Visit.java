package io.vlingo.xoom.petclinic.model;

public final class Visit {

  public final Date start;
  public final Date end;
  public final String description;

  public static Visit from(final Date start, final Date end, final String description) {
    return new Visit(start, end, description);
  }

  private Visit (final Date start, final Date end, final String description) {
    this.start = start;
    this.end = end;
    this.description = description;
  }

}