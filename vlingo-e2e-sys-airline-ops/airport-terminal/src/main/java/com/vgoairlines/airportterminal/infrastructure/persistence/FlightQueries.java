package com.vgoairlines.airportterminal.infrastructure.persistence;

import java.util.Collection;
import io.vlingo.common.Completes;

import com.vgoairlines.airportterminal.infrastructure.FlightData;

public interface FlightQueries {
  Completes<FlightData> flightOf(String id);
  Completes<Collection<FlightData>> flights();
}