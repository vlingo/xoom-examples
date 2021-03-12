package io.vlingo.developers.petclinic.infrastructure.persistence;

import java.util.Arrays;
import java.util.List;

import io.vlingo.developers.petclinic.model.specialtytype.SpecialtyTypeOffered;
import io.vlingo.developers.petclinic.model.veterinarian.VeterinarianRegistered;
import io.vlingo.developers.petclinic.model.veterinarian.VeterinarianNameChanged;
import io.vlingo.developers.petclinic.model.specialtytype.SpecialtyTypeRenamed;
import io.vlingo.developers.petclinic.model.veterinarian.VeterinarianSpecialtyChosen;
import io.vlingo.developers.petclinic.model.pet.PetNameChanged;
import io.vlingo.developers.petclinic.model.pet.PetDeathRecorded;
import io.vlingo.developers.petclinic.model.pet.PetKindCorrected;
import io.vlingo.developers.petclinic.model.client.ClientNameChanged;
import io.vlingo.developers.petclinic.model.veterinarian.VeterinarianContactInformationChanged;
import io.vlingo.developers.petclinic.model.animaltype.AnimalTypeRenamed;
import io.vlingo.developers.petclinic.model.pet.PetEntity;
import io.vlingo.developers.petclinic.model.animaltype.AnimalTypeTreatmentOffered;
import io.vlingo.developers.petclinic.model.animaltype.AnimalTypeEntity;
import io.vlingo.developers.petclinic.model.client.ClientEntity;
import io.vlingo.developers.petclinic.model.client.ClientContactInformationChanged;
import io.vlingo.developers.petclinic.model.pet.PetBirthRecorded;
import io.vlingo.developers.petclinic.model.veterinarian.VeterinarianEntity;
import io.vlingo.developers.petclinic.model.pet.PetOwnerChanged;
import io.vlingo.developers.petclinic.model.client.ClientRegistered;
import io.vlingo.developers.petclinic.model.specialtytype.SpecialtyTypeEntity;
import io.vlingo.developers.petclinic.model.pet.PetRegistered;

import io.vlingo.actors.Definition;
import io.vlingo.actors.Stage;
import io.vlingo.lattice.model.sourcing.SourcedTypeRegistry;
import io.vlingo.lattice.model.sourcing.SourcedTypeRegistry.Info;
import io.vlingo.symbio.EntryAdapterProvider;
import io.vlingo.symbio.store.dispatch.Dispatcher;
import io.vlingo.symbio.store.dispatch.NoOpDispatcher;
import io.vlingo.symbio.store.dispatch.DispatcherControl;
import io.vlingo.symbio.store.dispatch.Dispatchable;
import io.vlingo.symbio.store.journal.Journal;
import io.vlingo.xoom.actors.Settings;
import io.vlingo.xoom.storage.Model;
import io.vlingo.xoom.storage.StoreActorBuilder;
import io.vlingo.xoom.annotation.persistence.Persistence.StorageType;

public class CommandModelJournalProvider  {
  private static CommandModelJournalProvider instance;

  public final Journal<String> journal;

  public static CommandModelJournalProvider  instance() {
    return instance;
  }

  public static CommandModelJournalProvider using(final Stage stage, final SourcedTypeRegistry registry) {
    return using(stage, registry, new NoOpDispatcher());
 }

  @SuppressWarnings({ "unchecked", "unused" })
  public static CommandModelJournalProvider using(final Stage stage, final SourcedTypeRegistry registry, final Dispatcher ...dispatchers) {
    if (instance != null) {
      return instance;
    }

    final EntryAdapterProvider entryAdapterProvider = EntryAdapterProvider.instance(stage.world());

    entryAdapterProvider.registerAdapter(AnimalTypeRenamed.class, new AnimalTypeRenamedAdapter());
    entryAdapterProvider.registerAdapter(VeterinarianSpecialtyChosen.class, new VeterinarianSpecialtyChosenAdapter());
    entryAdapterProvider.registerAdapter(PetKindCorrected.class, new PetKindCorrectedAdapter());
    entryAdapterProvider.registerAdapter(PetRegistered.class, new PetRegisteredAdapter());
    entryAdapterProvider.registerAdapter(VeterinarianRegistered.class, new VeterinarianRegisteredAdapter());
    entryAdapterProvider.registerAdapter(PetOwnerChanged.class, new PetOwnerChangedAdapter());
    entryAdapterProvider.registerAdapter(ClientContactInformationChanged.class, new ClientContactInformationChangedAdapter());
    entryAdapterProvider.registerAdapter(VeterinarianNameChanged.class, new VeterinarianNameChangedAdapter());
    entryAdapterProvider.registerAdapter(SpecialtyTypeOffered.class, new SpecialtyTypeOfferedAdapter());
    entryAdapterProvider.registerAdapter(SpecialtyTypeRenamed.class, new SpecialtyTypeRenamedAdapter());
    entryAdapterProvider.registerAdapter(VeterinarianContactInformationChanged.class, new VeterinarianContactInformationChangedAdapter());
    entryAdapterProvider.registerAdapter(PetNameChanged.class, new PetNameChangedAdapter());
    entryAdapterProvider.registerAdapter(ClientRegistered.class, new ClientRegisteredAdapter());
    entryAdapterProvider.registerAdapter(ClientNameChanged.class, new ClientNameChangedAdapter());
    entryAdapterProvider.registerAdapter(PetDeathRecorded.class, new PetDeathRecordedAdapter());
    entryAdapterProvider.registerAdapter(PetBirthRecorded.class, new PetBirthRecordedAdapter());
    entryAdapterProvider.registerAdapter(AnimalTypeTreatmentOffered.class, new AnimalTypeTreatmentOfferedAdapter());

    final Journal<String> journal =
              StoreActorBuilder.from(stage, Model.COMMAND, Arrays.asList(dispatchers), StorageType.JOURNAL, Settings.properties(), true);

    registry.register(new Info(journal, ClientEntity.class, ClientEntity.class.getSimpleName()));
    registry.register(new Info(journal, AnimalTypeEntity.class, AnimalTypeEntity.class.getSimpleName()));
    registry.register(new Info(journal, VeterinarianEntity.class, VeterinarianEntity.class.getSimpleName()));
    registry.register(new Info(journal, SpecialtyTypeEntity.class, SpecialtyTypeEntity.class.getSimpleName()));
    registry.register(new Info(journal, PetEntity.class, PetEntity.class.getSimpleName()));

    instance = new CommandModelJournalProvider(journal);

    return instance;
  }

  private CommandModelJournalProvider(final Journal<String> journal) {
    this.journal = journal;
  }

}
