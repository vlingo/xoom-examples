// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.airtrafficcontrol.infrastructure;

import com.skyharbor.airtrafficcontrol.infrastructure.exchange.ExchangeBootstrap;
import io.vlingo.actors.Grid;
import io.vlingo.symbio.store.dispatch.Dispatcher;
import io.vlingo.xoom.XoomInitializationAware;
import io.vlingo.xoom.annotation.initializer.Xoom;

@Xoom(name = "air-traffic-control")
public class Bootstrap implements XoomInitializationAware {

  @Override
  public void onInit(final Grid grid) {
  }

  @Override
  public Dispatcher exchangeDispatcher(final Grid grid) {
     return ExchangeBootstrap.init(grid).dispatcher();
  }
}
