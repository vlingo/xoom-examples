package com.skyharbor.fleetcrew.infrastructure.exchange;

import io.vlingo.actors.Definition;
import io.vlingo.actors.Stage;
import io.vlingo.lattice.exchange.ExchangeReceiver;

import com.skyharbor.fleetcrew.model.aircraft.Aircraft;
import com.skyharbor.fleetcrew.infrastructure.AircraftData;
import com.skyharbor.fleetcrew.model.aircraft.AircraftEntity;

public class AircraftExchangeReceivers {

  static class FlightLanded implements ExchangeReceiver<AircraftData> {

    private final Stage stage;

    public FlightLanded(final Stage stage) {
      this.stage = stage;
    }

    @Override
    public void receive(final AircraftData data) {
      stage.actorOf(Aircraft.class, stage.addressFactory().from(data.id), Definition.has(AircraftEntity.class, Definition.parameters(data.id)))
              .andFinallyConsume(aircraft -> aircraft.recordArrival(data.carrier, data.flightNumber, data.tailNumber, data.gate));
    }
  }

}