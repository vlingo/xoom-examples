package com.skyharbor.aircraftmonitoring.infrastructure.exchange;

import com.skyharbor.aircraftmonitoring.model.flight.*;
import io.vlingo.actors.Definition;
import io.vlingo.actors.Stage;
import io.vlingo.lattice.exchange.ExchangeReceiver;

import com.skyharbor.aircraftmonitoring.infrastructure.FlightData;

public class FlightExchangeReceivers {

  static class FlightDepartedGate implements ExchangeReceiver<FlightData> {

    private final Stage stage;

    public FlightDepartedGate(final Stage stage) {
      this.stage = stage;
    }

    @Override
    public void receive(final FlightData data) {
      stage.actorOf(Flight.class, stage.addressFactory().from(data.id), Definition.has(FlightEntity.class, Definition.parameters(data.id)))
              .andFinallyConsume(flight -> {
                final Aircraft aircraft =
                        Aircraft.of(data.aircraft.tailNumber, data.aircraft.carrier);

                final ActualDeparture actualDeparture =
                        ActualDeparture.at("", data.actualDeparture.occurredOn);

                final EstimatedArrival estimatedArrival =
                        EstimatedArrival.at(data.estimatedArrival.time);

                flight.departGate(aircraft, actualDeparture, estimatedArrival);
              });
    }
  }

  static class FlightTookOff implements ExchangeReceiver<FlightData> {

    private final Stage stage;

    public FlightTookOff(final Stage stage) {
      this.stage = stage;
    }

    @Override
    public void receive(final FlightData data) {
      stage.actorOf(Flight.class, stage.addressFactory().from(data.id), Definition.has(FlightEntity.class, Definition.parameters(data.id)))
              .andFinallyConsume(flight -> flight.takeOff());
    }
  }

  static class FlightLanded implements ExchangeReceiver<FlightData> {

    private final Stage stage;

    public FlightLanded(final Stage stage) {
      this.stage = stage;
    }

    @Override
    public void receive(final FlightData data) {
      stage.actorOf(Flight.class, stage.addressFactory().from(data.id), Definition.has(FlightEntity.class, Definition.parameters(data.id)))
              .andFinallyConsume(flight -> flight.land());
    }
  }

}