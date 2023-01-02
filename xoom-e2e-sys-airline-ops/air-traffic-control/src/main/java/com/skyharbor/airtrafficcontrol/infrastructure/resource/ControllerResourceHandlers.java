// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.airtrafficcontrol.infrastructure.resource;

import com.skyharbor.airtrafficcontrol.infrastructure.ControllerData;
import com.skyharbor.airtrafficcontrol.infrastructure.persistence.ControllerQueries;
import com.skyharbor.airtrafficcontrol.model.controller.Controller;
import com.skyharbor.airtrafficcontrol.model.controller.ControllerState;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.turbo.annotation.autodispatch.Handler.Three;
import io.vlingo.xoom.turbo.annotation.autodispatch.Handler.Two;
import io.vlingo.xoom.turbo.annotation.autodispatch.HandlerEntry;

import java.util.Collection;

public class ControllerResourceHandlers {

  public static final int AUTHORIZE = 0;
  public static final int CONTROLLERS = 1;
  public static final int ADAPT_STATE = 2;

  public static final HandlerEntry<Three<Completes<ControllerState>, Stage, ControllerData>> AUTHORIZE_HANDLER =
          HandlerEntry.of(AUTHORIZE, ($stage, data) -> Controller.authorize($stage, data.name));

  public static final HandlerEntry<Two<ControllerData, ControllerState>> ADAPT_STATE_HANDLER =
          HandlerEntry.of(ADAPT_STATE, ControllerData::from);

  public static final HandlerEntry<Two<Completes<Collection<ControllerData>>, ControllerQueries>> QUERY_ALL_HANDLER =
          HandlerEntry.of(CONTROLLERS, ControllerQueries::controllers);

}