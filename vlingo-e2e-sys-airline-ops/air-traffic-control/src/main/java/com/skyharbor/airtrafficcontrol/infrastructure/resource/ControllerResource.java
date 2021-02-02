// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.airtrafficcontrol.infrastructure.resource;

import io.vlingo.common.Completes;
import io.vlingo.xoom.annotation.autodispatch.*;
import io.vlingo.http.Response;

import com.skyharbor.airtrafficcontrol.infrastructure.ControllerData;
import com.skyharbor.airtrafficcontrol.model.controller.ControllerEntity;
import com.skyharbor.airtrafficcontrol.infrastructure.persistence.ControllerQueriesActor;
import com.skyharbor.airtrafficcontrol.model.controller.Controller;
import com.skyharbor.airtrafficcontrol.infrastructure.persistence.ControllerQueries;

import static io.vlingo.http.Method.*;

@AutoDispatch(path="/controllers", handlers=ControllerResourceHandlers.class)
@Queries(protocol = ControllerQueries.class, actor = ControllerQueriesActor.class)
@Model(protocol = Controller.class, actor = ControllerEntity.class, data = ControllerData.class)
public interface ControllerResource {

  @Route(method = POST, path = "/", handler = ControllerResourceHandlers.AUTHORIZE)
  @ResponseAdapter(handler = ControllerResourceHandlers.ADAPT_STATE)
  Completes<Response> authorize(@Body final ControllerData data);

  @Route(method = GET, handler = ControllerResourceHandlers.CONTROLLERS)
  Completes<Response> controllers();

}