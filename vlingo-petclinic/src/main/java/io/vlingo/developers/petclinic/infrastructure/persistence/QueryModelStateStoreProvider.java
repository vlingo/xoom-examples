package io.vlingo.developers.petclinic.infrastructure.persistence;

import java.util.Arrays;
import java.util.List;

import io.vlingo.developers.petclinic.infrastructure.OwnerData;
import io.vlingo.developers.petclinic.infrastructure.persistence.AnimalTypeQueriesActor;
import io.vlingo.developers.petclinic.infrastructure.ContactInformationData;
import io.vlingo.developers.petclinic.model.animaltype.AnimalTypeEntity;
import io.vlingo.developers.petclinic.infrastructure.PetData;
import io.vlingo.developers.petclinic.infrastructure.persistence.AnimalTypeQueries;
import io.vlingo.developers.petclinic.infrastructure.persistence.PetQueriesActor;
import io.vlingo.developers.petclinic.infrastructure.SpecialtyTypeData;
import io.vlingo.developers.petclinic.infrastructure.persistence.SpecialtyTypeQueriesActor;
import io.vlingo.developers.petclinic.infrastructure.persistence.VeterinarianQueries;
import io.vlingo.developers.petclinic.model.veterinarian.VeterinarianEntity;
import io.vlingo.developers.petclinic.infrastructure.PostalAddressData;
import io.vlingo.developers.petclinic.infrastructure.FullnameData;
import io.vlingo.developers.petclinic.infrastructure.AnimalTypeData;
import io.vlingo.developers.petclinic.infrastructure.persistence.PetQueries;
import io.vlingo.developers.petclinic.infrastructure.SpecialtyData;
import io.vlingo.developers.petclinic.infrastructure.persistence.VeterinarianQueriesActor;
import io.vlingo.developers.petclinic.infrastructure.persistence.SpecialtyTypeQueries;
import io.vlingo.developers.petclinic.infrastructure.VisitData;
import io.vlingo.developers.petclinic.infrastructure.NameData;
import io.vlingo.developers.petclinic.infrastructure.persistence.ClientQueriesActor;
import io.vlingo.developers.petclinic.infrastructure.KindData;
import io.vlingo.developers.petclinic.infrastructure.persistence.ClientQueries;
import io.vlingo.developers.petclinic.model.pet.PetEntity;
import io.vlingo.developers.petclinic.infrastructure.VeterinarianData;
import io.vlingo.developers.petclinic.infrastructure.TelephoneData;
import io.vlingo.developers.petclinic.model.client.ClientEntity;
import io.vlingo.developers.petclinic.model.specialtytype.SpecialtyTypeEntity;
import io.vlingo.developers.petclinic.infrastructure.ClientData;

import io.vlingo.actors.Definition;
import io.vlingo.actors.Protocols;
import io.vlingo.actors.Stage;
import io.vlingo.symbio.store.state.StateTypeStateStoreMap;
import io.vlingo.lattice.model.stateful.StatefulTypeRegistry;
import io.vlingo.symbio.EntryAdapterProvider;
import io.vlingo.symbio.store.dispatch.Dispatcher;
import io.vlingo.symbio.store.dispatch.NoOpDispatcher;
import io.vlingo.symbio.store.dispatch.DispatcherControl;
import io.vlingo.symbio.store.dispatch.Dispatchable;
import io.vlingo.symbio.store.state.StateStore;
import io.vlingo.xoom.actors.Settings;
import io.vlingo.xoom.storage.Model;
import io.vlingo.xoom.storage.StoreActorBuilder;
import io.vlingo.xoom.annotation.persistence.Persistence.StorageType;


public class QueryModelStateStoreProvider {
  private static QueryModelStateStoreProvider instance;

  public final StateStore store;
  public final SpecialtyTypeQueries specialtyTypeQueries;
  public final AnimalTypeQueries animalTypeQueries;
  public final VeterinarianQueries veterinarianQueries;
  public final ClientQueries clientQueries;
  public final PetQueries petQueries;

  public static QueryModelStateStoreProvider instance() {
    return instance;
  }

  public static QueryModelStateStoreProvider using(final Stage stage, final StatefulTypeRegistry registry) {
    return using(stage, registry, new NoOpDispatcher());
  }

  @SuppressWarnings("rawtypes")
  public static QueryModelStateStoreProvider using(final Stage stage, final StatefulTypeRegistry registry, final Dispatcher ...dispatchers) {
    if (instance != null) {
      return instance;
    }

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


    instance = new QueryModelStateStoreProvider(stage, store);

    return instance;
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
