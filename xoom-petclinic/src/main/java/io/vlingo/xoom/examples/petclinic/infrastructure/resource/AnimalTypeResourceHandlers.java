package io.vlingo.xoom.examples.petclinic.infrastructure.resource;

import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.turbo.annotation.autodispatch.Handler.Three;
import io.vlingo.xoom.turbo.annotation.autodispatch.Handler.Two;
import io.vlingo.xoom.turbo.annotation.autodispatch.HandlerEntry;

import io.vlingo.xoom.examples.petclinic.infrastructure.AnimalTypeData;
import io.vlingo.xoom.examples.petclinic.infrastructure.persistence.AnimalTypeQueries;
import io.vlingo.xoom.examples.petclinic.model.animaltype.AnimalTypeState;
import io.vlingo.xoom.examples.petclinic.model.animaltype.AnimalType;
import java.util.Collection;

public class AnimalTypeResourceHandlers {

  public static final int OFFER_TREATMENT_FOR = 0;
  public static final int RENAME = 1;
  public static final int ANIMAL_TYPES = 2;
  public static final int ADAPT_STATE = 3;

  public static final HandlerEntry<Three<Completes<AnimalTypeState>, Stage, AnimalTypeData>> OFFER_TREATMENT_FOR_HANDLER =
          HandlerEntry.of(OFFER_TREATMENT_FOR, ($stage, data) -> {
              return AnimalType.offerTreatmentFor($stage, data.name);
          });

  public static final HandlerEntry<Three<Completes<AnimalTypeState>, AnimalType, AnimalTypeData>> RENAME_HANDLER =
          HandlerEntry.of(RENAME, (animalType, data) -> {
              return animalType.rename(data.name);
          });

  public static final HandlerEntry<Two<AnimalTypeData, AnimalTypeState>> ADAPT_STATE_HANDLER =
          HandlerEntry.of(ADAPT_STATE, AnimalTypeData::from);

  public static final HandlerEntry<Two<Completes<Collection<AnimalTypeData>>, AnimalTypeQueries>> QUERY_ALL_HANDLER =
          HandlerEntry.of(ANIMAL_TYPES, AnimalTypeQueries::animalTypes);

}