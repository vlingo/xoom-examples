package com.skyharbor.airtrafficcontrol.infrastructure.persistence;

import java.util.Collection;
import io.vlingo.common.Completes;

import com.skyharbor.airtrafficcontrol.infrastructure.FlightData;

public interface FlightQueries {
  Completes<FlightData> flightOf(String id);
  Completes<Collection<FlightData>> flights();
}