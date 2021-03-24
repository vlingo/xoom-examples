package io.vlingo.developers.petclinic.infrastructure.persistence;

import io.vlingo.actors.Stage;
import io.vlingo.developers.petclinic.infrastructure.*;
import io.vlingo.lattice.model.stateful.StatefulTypeRegistry;
import io.vlingo.symbio.EntryAdapterProvider;
import io.vlingo.symbio.store.dispatch.Dispatcher;
import io.vlingo.symbio.store.dispatch.NoOpDispatcher;
import io.vlingo.symbio.store.state.StateStore;
import io.vlingo.symbio.store.state.StateTypeStateStoreMap;
import io.vlingo.xoom.actors.Settings;
import io.vlingo.xoom.annotation.persistence.Persistence.StorageType;
import io.vlingo.xoom.storage.Model;
import io.vlingo.xoom.storage.StoreActorBuilder;

import java.util.Arrays;

public class QueryModelStateStoreProvider {
  public final StateStore store;
  public final SpecialtyTypeQueries specialtyTypeQueries;
  public final AnimalTypeQueries animalTypeQueries;
  public final VeterinarianQueries veterinarianQueries;
  public final ClientQueries clientQueries;
  public final PetQueries petQueries;

  public static QueryModelStateStoreProvider using(final Stage stage, final StatefulTypeRegistry registry) {
    return using(stage, registry, new NoOpDispatcher());
  }

  @SuppressWarnings("rawtypes")
  public static QueryModelStateStoreProvider using(final Stage stage, final StatefulTypeRegistry registry, final Dispatcher ...dispatchers) {

    new EntryAdapterProvider(stage.world()); // future use

    StateTypeStateStoreMap.stateTypeToStoreName(VisitData.class, VisitData.class.getSimpleName());
    StateTypeStateStoreMap.stateTypeToStoreName(TelephoneData.class, TelephoneData.class.getSimpleName());
    StateTypeStateStoreMap.stateTypeToStoreName(KindData.class, KindData.class.getSimpleName());
    StateTypeStateStoreMap.stateTypeToStoreName(ContactInformationData.class, ContactInformationData.class.getSimpleName());
    StateTypeStateStoreMap.stateTypeToStoreName(FullnameData.class, FullnameData.class.getSimpleName());
    StateTypeStateStoreMap.stateTypeToStoreName(PetData.class, PetData.class.getSimpleName());
    StateTypeStateStoreMap.stateTypeToStoreName(SpecialtyTypeData.class, SpecialtyTypeData.class.getSimpleName());
    StateTypeStateStoreMap.stateTypeToStoreName(VeterinarianData.class, VeterinarianData.class.getSimpleName());
    StateTypeStateStoreMap.stateTypeToStoreName(OwnerData.class, OwnerData.class.getSimpleName());
    StateTypeStateStoreMap.stateTypeToStoreName(AnimalTypeData.class, AnimalTypeData.class.getSimpleName());
    StateTypeStateStoreMap.stateTypeToStoreName(PostalAddressData.class, PostalAddressData.class.getSimpleName());
    StateTypeStateStoreMap.stateTypeToStoreName(ClientData.class, ClientData.class.getSimpleName());
    StateTypeStateStoreMap.stateTypeToStoreName(SpecialtyData.class, SpecialtyData.class.getSimpleName());
    StateTypeStateStoreMap.stateTypeToStoreName(NameData.class, NameData.class.getSimpleName());

    final StateStore store =
            StoreActorBuilder.from(stage, Model.QUERY, Arrays.asList(dispatchers), StorageType.STATE_STORE, Settings.properties(), true);


    return new QueryModelStateStoreProvider(stage, store);
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  private QueryModelStateStoreProvider(final Stage stage, final StateStore store) {
    this.store = store;
    this.specialtyTypeQueries = stage.actorFor(SpecialtyTypeQueries.class, SpecialtyTypeQueriesActor.class, store);
    this.animalTypeQueries = stage.actorFor(AnimalTypeQueries.class, AnimalTypeQueriesActor.class, store);
    this.veterinarianQueries = stage.actorFor(VeterinarianQueries.class, VeterinarianQueriesActor.class, store);
    this.clientQueries = stage.actorFor(ClientQueries.class, ClientQueriesActor.class, store);
    this.petQueries = stage.actorFor(PetQueries.class, PetQueriesActor.class, store);
  }
}
