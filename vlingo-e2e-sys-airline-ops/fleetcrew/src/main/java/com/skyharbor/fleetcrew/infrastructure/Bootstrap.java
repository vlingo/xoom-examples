package com.skyharbor.fleetcrew.infrastructure;

import com.skyharbor.fleetcrew.infrastructure.exchange.ExchangeBootstrap;

import io.vlingo.actors.Stage;
import io.vlingo.xoom.XoomInitializationAware;
import io.vlingo.xoom.annotation.initializer.AddressFactory;
import io.vlingo.xoom.annotation.initializer.Xoom;

import static io.vlingo.xoom.annotation.initializer.AddressFactory.Type.UUID;

@Xoom(name = "fleetcrew", addressFactory = @AddressFactory(type = UUID))
public class Bootstrap implements XoomInitializationAware {

  @Override
  public void onInit(final Stage stage) {
  }

  @Override
  public io.vlingo.symbio.store.dispatch.Dispatcher exchangeDispatcher(final Stage stage) {
     return ExchangeBootstrap.init(stage).dispatcher();
  }
}
