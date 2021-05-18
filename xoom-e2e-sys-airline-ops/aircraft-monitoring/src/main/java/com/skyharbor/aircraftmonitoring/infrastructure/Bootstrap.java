// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.aircraftmonitoring.infrastructure;

import io.vlingo.xoom.cluster.model.Properties;
import io.vlingo.xoom.lattice.grid.Grid;
import io.vlingo.xoom.turbo.XoomInitializationAware;
import io.vlingo.xoom.turbo.annotation.initializer.Xoom;

@Xoom(name = "aircraft-monitoring")
public class Bootstrap implements XoomInitializationAware {

  @Override
  public void onInit(final Grid stage) {
  }

  @Override
  public Properties clusterProperties() {
    return Properties.instance();
  }
}
