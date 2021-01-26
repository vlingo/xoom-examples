package com.vgoairlines.flightplanning.infrastructure.persistence;

import com.vgoairlines.flightplanning.infrastructure.FlightData;
import io.vlingo.common.Completes;

import java.util.Collection;

public interface FlightQueries {
  Completes<FlightData> flightOf(String id);
  Completes<Collection<FlightData>> flights();
}