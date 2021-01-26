package com.vgoairlines.flightplanning.infrastructure.resource;

import com.vgoairlines.flightplanning.infrastructure.FlightData;
import com.vgoairlines.flightplanning.infrastructure.persistence.FlightQueries;
import com.vgoairlines.flightplanning.model.flight.*;
import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;
import io.vlingo.xoom.annotation.autodispatch.Handler.Three;
import io.vlingo.xoom.annotation.autodispatch.Handler.Two;
import io.vlingo.xoom.annotation.autodispatch.HandlerEntry;

import java.util.Collection;

public class FlightResourceHandlers {

  public static final int SCHEDULE = 0;
  public static final int POOL = 1;
  public static final int RESCHEDULE = 2;
  public static final int CANCEL = 3;
  public static final int FLIGHTS = 4;
  public static final int ADAPT_STATE = 5;

  public static final HandlerEntry<Three<Completes<FlightState>, Stage, FlightData>> SCHEDULE_HANDLER =
          HandlerEntry.of(SCHEDULE, ($stage, data) -> {
            final Airport departureAirport =
                    Airport.with(data.schedule.departure.airport.code);

            final Airport arrivalAirport =
                    Airport.with(data.schedule.arrival.airport.code);

            final Schedule schedule =
                    Schedule.with(Departure.on(departureAirport, data.schedule.departure.plannedFor),
                            Arrival.on(arrivalAirport, data.schedule.arrival.plannedFor));

            return Flight.schedule($stage, schedule);
          });

  public static final HandlerEntry<Three<Completes<FlightState>, Flight, FlightData>> POOL_HANDLER =
          HandlerEntry.of(POOL, (flight, data) ->
              flight.pool(Aircraft.of(data.aircraft.id, data.aircraft.model,
                      data.aircraft.serialNumber, data.aircraft.tailNumber)));

  public static final HandlerEntry<Three<Completes<FlightState>, Flight, FlightData>> RESCHEDULE_HANDLER =
          HandlerEntry.of(RESCHEDULE, (flight, data) -> {
              final Airport departureAirport =
                      Airport.with(data.schedule.departure.airport.code);

              final Airport arrivalAirport =
                      Airport.with(data.schedule.arrival.airport.code);

              final Schedule schedule =
                      Schedule.with(Departure.on(departureAirport, data.schedule.departure.plannedFor),
                              Arrival.on(arrivalAirport, data.schedule.arrival.plannedFor));

              return flight.reschedule(schedule);
          });

  public static final HandlerEntry<Three<Completes<FlightState>, Flight, FlightData>> CANCEL_HANDLER =
          HandlerEntry.of(CANCEL, (flight, data) -> flight.cancel());

  public static final HandlerEntry<Two<FlightData, FlightState>> ADAPT_STATE_HANDLER =
          HandlerEntry.of(ADAPT_STATE, FlightData::from);

  public static final HandlerEntry<Two<Completes<Collection<FlightData>>, FlightQueries>> QUERY_ALL_HANDLER =
          HandlerEntry.of(FLIGHTS, FlightQueries::flights);

}