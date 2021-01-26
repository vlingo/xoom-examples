package com.skyharbor.airtrafficcontrol.infrastructure.exchange;

import io.vlingo.lattice.exchange.ExchangeAdapter;
import io.vlingo.lattice.exchange.MessageParameters;
import io.vlingo.lattice.exchange.MessageParameters.DeliveryMode;
import io.vlingo.lattice.exchange.rabbitmq.Message;

import com.skyharbor.airtrafficcontrol.infrastructure.ControllerData;

public class ControllerConsumerAdapter implements ExchangeAdapter<ControllerData, String, Message> {

  private final String supportedSchemaName;

  public ControllerConsumerAdapter(final String supportedSchemaName) {
    this.supportedSchemaName = supportedSchemaName;
  }

  @Override
  public ControllerData fromExchange(final Message exchangeMessage) {
    return new ControllerDataMapper().externalToLocal(exchangeMessage.payloadAsText());
  }

  @Override
  public Message toExchange(final ControllerData local) {
    final String messagePayload = new ControllerDataMapper().localToExternal(local);
    return new Message(messagePayload, MessageParameters.bare().deliveryMode(DeliveryMode.Durable));
  }

  @Override
  public boolean supports(final Object exchangeMessage) {
    if(!exchangeMessage.getClass().equals(Message.class)) {
      return false;
    }
    final String schemaName = ((Message) exchangeMessage).messageParameters.typeName();
    return supportedSchemaName.equalsIgnoreCase(schemaName);
  }

}