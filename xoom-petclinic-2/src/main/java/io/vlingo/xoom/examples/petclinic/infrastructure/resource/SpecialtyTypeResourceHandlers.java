package io.vlingo.xoom.examples.petclinic.infrastructure.resource;

import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.turbo.annotation.autodispatch.Handler.Three;
import io.vlingo.xoom.turbo.annotation.autodispatch.Handler.Two;
import io.vlingo.xoom.turbo.annotation.autodispatch.HandlerEntry;

import io.vlingo.xoom.examples.petclinic.model.specialtytype.SpecialtyType;
import io.vlingo.xoom.examples.petclinic.infrastructure.SpecialtyTypeData;
import io.vlingo.xoom.examples.petclinic.infrastructure.persistence.SpecialtyTypeQueries;
import io.vlingo.xoom.examples.petclinic.model.specialtytype.SpecialtyTypeState;
import java.util.Collection;

public class SpecialtyTypeResourceHandlers {

  public static final int OFFER = 0;
  public static final int RENAME = 1;
  public static final int SPECIALTY_TYPES = 2;
  public static final int ADAPT_STATE = 3;

  public static final HandlerEntry<Three<Completes<SpecialtyTypeState>, Stage, SpecialtyTypeData>> OFFER_HANDLER =
          HandlerEntry.of(OFFER, ($stage, data) -> {
              return SpecialtyType.offer($stage, data.name);
          });

  public static final HandlerEntry<Three<Completes<SpecialtyTypeState>, SpecialtyType, SpecialtyTypeData>> RENAME_HANDLER =
          HandlerEntry.of(RENAME, (specialtyType, data) -> {
              return specialtyType.rename(data.name);
          });

  public static final HandlerEntry<Two<SpecialtyTypeData, SpecialtyTypeState>> ADAPT_STATE_HANDLER =
          HandlerEntry.of(ADAPT_STATE, SpecialtyTypeData::from);

  public static final HandlerEntry<Two<Completes<Collection<SpecialtyTypeData>>, SpecialtyTypeQueries>> QUERY_ALL_HANDLER =
          HandlerEntry.of(SPECIALTY_TYPES, SpecialtyTypeQueries::specialtyTypes);

}