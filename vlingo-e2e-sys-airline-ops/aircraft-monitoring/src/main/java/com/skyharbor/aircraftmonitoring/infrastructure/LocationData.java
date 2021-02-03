// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.aircraftmonitoring.infrastructure;

public class LocationData {

  public final String altitude;
  public final String latitude;
  public final String longitude;

  public LocationData(final String altitude,
                      final String latitude,
                      final String longitude) {
    this.altitude = altitude;
    this.latitude = latitude;
    this.longitude = longitude;
  }
}
