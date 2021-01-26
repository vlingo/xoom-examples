package com.vgoairlines.flightplanning.infrastructure;

import com.vgoairlines.flightplanning.model.flight.FlightState;

import java.util.List;
import java.util.stream.Collectors;

public class FlightData {
  public final String id;
  public final AircraftData aircraft;
  public final ScheduleData schedule;
  public final boolean cancelled;

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
    this.aircraft =
            new AircraftData(state.aircraft.id, state.aircraft.model,
                    state.aircraft.serialNumber, state.aircraft.tailNumber);
    this.schedule =
            new ScheduleData(
                    new DepartureData(
                            new AirportData(state.schedule.departure.airport.code),
                            state.schedule.departure.plannedFor),
                    new ArrivalData(
                            new AirportData(state.schedule.arrival.airport.code),
                            state.schedule.departure.plannedFor)
            );

    this.cancelled = state.cancelled;
  }

}
