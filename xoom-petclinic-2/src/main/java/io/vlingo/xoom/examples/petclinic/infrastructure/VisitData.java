package io.vlingo.xoom.examples.petclinic.infrastructure;

import io.vlingo.xoom.examples.petclinic.model.Visit;

public class VisitData {

  public final DateData start;
  public final DateData end;
  public final String description;

  public static VisitData from(final Visit visit) {
    final DateData start = visit.start != null ? DateData.from(visit.start) : null;
    final DateData end = visit.end != null ? DateData.from(visit.end) : null;
    return from(start, end, visit.description);
  }

  public static VisitData from(final DateData start, final DateData end, final String description) {
    return new VisitData(start, end, description);
  }

  private VisitData (final DateData start, final DateData end, final String description) {
    this.start = start;
    this.end = end;
    this.description = description;
  }

}