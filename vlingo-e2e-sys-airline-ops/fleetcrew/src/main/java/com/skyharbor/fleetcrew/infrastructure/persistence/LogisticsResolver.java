// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.skyharbor.fleetcrew.infrastructure.persistence;

import java.util.Arrays;
import java.util.Random;

//TODO: Make LogisticsResolver an actor-based DomainService
public class LogisticsResolver {

  public static String availableGate() {
    return Arrays.asList("A", "B", "C").get(new Random().nextInt(3));
  }

  public static String availableCarrier() {
    return Arrays.asList("John", "Bruce", "Robert").get(new Random().nextInt(3));
  }
}
