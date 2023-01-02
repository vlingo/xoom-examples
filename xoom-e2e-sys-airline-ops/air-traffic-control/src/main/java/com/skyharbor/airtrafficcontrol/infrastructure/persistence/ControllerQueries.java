// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.airtrafficcontrol.infrastructure.persistence;

import com.skyharbor.airtrafficcontrol.infrastructure.ControllerData;
import io.vlingo.xoom.common.Completes;

import java.util.Collection;

public interface ControllerQueries {
  Completes<ControllerData> controllerOf(String id);
  Completes<Collection<ControllerData>> controllers();
}