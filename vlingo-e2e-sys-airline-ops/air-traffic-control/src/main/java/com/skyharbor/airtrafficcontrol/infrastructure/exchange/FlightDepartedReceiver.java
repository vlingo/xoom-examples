package com.skyharbor.airtrafficcontrol.infrastructure.exchange;

import com.skyharbor.airtrafficcontrol.model.flight.Flight;
import com.vgoairlines.airportterminal.event.FlightDeparted;
import io.vlingo.actors.Stage;
import io.vlingo.lattice.exchange.ExchangeReceiver;

public class FlightDepartedReceiver implements ExchangeReceiver<FlightDeparted> {

  private final Stage stage;

  public FlightDepartedReceiver(final Stage stage) {
    this.stage = stage;
  }

  @Override
  public void receive(final FlightDeparted event) {
    Flight.departGate(stage, event.aircraftId, event.number, event.tailNumber, event.equipment);
  }
}