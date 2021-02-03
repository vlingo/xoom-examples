// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.fleetcrew.infrastructure.persistence;

import java.util.Collection;
import io.vlingo.common.Completes;

import com.skyharbor.fleetcrew.infrastructure.AircraftData;

public interface AircraftQueries {
  Completes<AircraftData> aircraftOf(String id);
  Completes<Collection<AircraftData>> aircrafts();
}