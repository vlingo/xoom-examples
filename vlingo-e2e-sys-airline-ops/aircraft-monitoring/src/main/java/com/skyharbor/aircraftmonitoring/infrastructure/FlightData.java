package com.skyharbor.aircraftmonitoring.infrastructure;

import com.skyharbor.aircraftmonitoring.model.flight.FlightState;

import java.util.List;
import java.util.stream.Collectors;

public class FlightData {
  public final String id;
  public final String status;
  public final AircraftData aircraft;
  public final ActualDepartureData actualDeparture;
  public final EstimatedArrivalData estimatedArrival;
  public final ActualArrivalData actualArrival;
  public final LocationData location;

  public static FlightData from(final FlightState state) {
    return new FlightData(state);
  }

  public static List<FlightData> from(final List<FlightState> states) {
    return states.stream().map(FlightData::from).collect(Collectors.toList());
  }

  public static FlightData empty() {
    return new FlightData(FlightState.identifiedBy(null));
  }

  private FlightData (final FlightState state) {
    this.id = state.id;
    this.status = state.status.name();
    this.aircraft = new AircraftData(state.aircraft.tailNumber, state.aircraft.carrier);
    this.actualDeparture = new ActualDepartureData(state.actualDeparture.occurredOn);
    this.estimatedArrival = new EstimatedArrivalData(state.estimatedArrival.time);
    this.actualArrival = new ActualArrivalData(state.actualArrival.occurredOn);
    this.location = new LocationData(state.location.altitude,
            state.location.latitude, state.location.longitude);
  }

}
