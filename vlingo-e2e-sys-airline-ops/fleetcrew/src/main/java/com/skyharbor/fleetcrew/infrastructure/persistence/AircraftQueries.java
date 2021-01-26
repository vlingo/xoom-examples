package com.skyharbor.fleetcrew.infrastructure.persistence;

import java.util.Collection;
import io.vlingo.common.Completes;

import com.skyharbor.fleetcrew.infrastructure.AircraftData;

public interface AircraftQueries {
  Completes<AircraftData> aircraftOf(String id);
  Completes<Collection<AircraftData>> aircrafts();
}