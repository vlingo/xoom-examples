// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.vgoairlines.inventory.infrastructure.resource;

import com.vgoairlines.inventory.infrastructure.AircraftData;
import com.vgoairlines.inventory.infrastructure.persistence.AircraftQueries;
import com.vgoairlines.inventory.model.aircraft.*;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.turbo.annotation.autodispatch.Handler.Three;
import io.vlingo.xoom.turbo.annotation.autodispatch.Handler.Two;
import io.vlingo.xoom.turbo.annotation.autodispatch.HandlerEntry;

import java.util.Collection;

public class AircraftResourceHandlers {

  public static final int CONSIGN = 0;
  public static final int AIRCRAFTS = 1;
  public static final int ADAPT_STATE = 2;

  public static final HandlerEntry<Three<Completes<AircraftState>, Stage, AircraftData>> CONSIGN_HANDLER =
          HandlerEntry.of(CONSIGN, ($stage, data) -> {
            final Registration registration =
                    Registration.of(data.registration.tailNumber);

            final ManufacturerSpecification manufacturerSpecification =
                    ManufacturerSpecification.of(data.manufacturerSpecification.manufacturer,
                            data.manufacturerSpecification.model, data.manufacturerSpecification.serialNumber);

            final Carrier carrier = Carrier.of(data.carrier.name, CarrierType.valueOf(data.carrier.type));

            return Aircraft.consign($stage, registration, manufacturerSpecification, carrier);
          });

  public static final HandlerEntry<Two<AircraftData, AircraftState>> ADAPT_STATE_HANDLER =
          HandlerEntry.of(ADAPT_STATE, AircraftData::from);

  public static final HandlerEntry<Two<Completes<Collection<AircraftData>>, AircraftQueries>> QUERY_ALL_HANDLER =
          HandlerEntry.of(AIRCRAFTS, AircraftQueries::aircrafts);

}