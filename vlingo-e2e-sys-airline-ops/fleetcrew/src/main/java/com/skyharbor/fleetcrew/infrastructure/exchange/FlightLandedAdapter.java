package com.skyharbor.fleetcrew.infrastructure.exchange;

import com.skyharbor.airtrafficcontrol.event.FlightLanded;
import io.vlingo.lattice.exchange.ExchangeAdapter;
import io.vlingo.lattice.exchange.MessageParameters;
import io.vlingo.lattice.exchange.MessageParameters.DeliveryMode;
import io.vlingo.lattice.exchange.rabbitmq.Message;

public class FlightLandedAdapter implements ExchangeAdapter<FlightLanded, String, Message> {

  @Override
  public FlightLanded fromExchange(final Message exchangeMessage) {
    return new FlightLandedMapper().externalToLocal(exchangeMessage.payloadAsText());
  }

  @Override
  public Message toExchange(final FlightLanded local) {
    final String messagePayload = new FlightLandedMapper().localToExternal(local);
    return new Message(messagePayload, MessageParameters.bare().deliveryMode(DeliveryMode.Durable));
  }

  @Override
  public boolean supports(final Object exchangeMessage) {
    if(!exchangeMessage.getClass().equals(Message.class)) {
      return false;
    }
    final String schemaName = ((Message) exchangeMessage).messageParameters.typeName();
    return SchemaReferences.FLIGHT_LANDED.match(schemaName);
  }

}