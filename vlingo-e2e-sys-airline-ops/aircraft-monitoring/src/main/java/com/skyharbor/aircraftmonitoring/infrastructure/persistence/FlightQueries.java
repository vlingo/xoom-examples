package com.skyharbor.aircraftmonitoring.infrastructure.persistence;

import java.util.Collection;
import io.vlingo.common.Completes;

import com.skyharbor.aircraftmonitoring.infrastructure.FlightData;

public interface FlightQueries {
  Completes<FlightData> flightOf(String id);
  Completes<Collection<FlightData>> flights();
}