// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.fleetcrew.infrastructure.persistence;

import com.skyharbor.fleetcrew.infrastructure.AircraftData;
import io.vlingo.xoom.common.Completes;

import java.util.Collection;

public interface AircraftQueries {
  Completes<AircraftData> aircraftOf(String id);
  Completes<Collection<AircraftData>> aircrafts();
}