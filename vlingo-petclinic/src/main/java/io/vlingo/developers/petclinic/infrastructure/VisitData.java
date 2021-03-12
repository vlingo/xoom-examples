package io.vlingo.developers.petclinic.infrastructure;

public class VisitData {

  public final long start;
  public final long end;
  public final String description;

  public static VisitData of(final long start, final long end, final String description) {
    return new VisitData(start, end, description);
  }

  private VisitData (final long start, final long end, final String description) {
    this.start = start;
    this.end = end;
    this.description = description;
  }

}