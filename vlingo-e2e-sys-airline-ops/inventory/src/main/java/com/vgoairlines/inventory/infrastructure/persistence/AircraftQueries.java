package com.vgoairlines.inventory.infrastructure.persistence;

import java.util.Collection;
import io.vlingo.common.Completes;

import com.vgoairlines.inventory.infrastructure.AircraftData;

public interface AircraftQueries {
  Completes<AircraftData> aircraftOf(String id);
  Completes<Collection<AircraftData>> aircrafts();
}