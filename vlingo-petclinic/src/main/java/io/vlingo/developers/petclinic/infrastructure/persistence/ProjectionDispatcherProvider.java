package io.vlingo.developers.petclinic.infrastructure.persistence;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import io.vlingo.actors.Definition;
import io.vlingo.actors.Protocols;
import io.vlingo.actors.Stage;
import io.vlingo.lattice.model.projection.ProjectionDispatcher;
import io.vlingo.lattice.model.projection.ProjectionDispatcher.ProjectToDescription;
import io.vlingo.lattice.model.projection.TextProjectionDispatcherActor;
import io.vlingo.symbio.store.dispatch.Dispatcher;

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
import io.vlingo.developers.petclinic.model.animaltype.AnimalTypeTreatmentOffered;
import io.vlingo.developers.petclinic.model.client.ClientContactInformationChanged;
import io.vlingo.developers.petclinic.model.pet.PetBirthRecorded;
import io.vlingo.developers.petclinic.model.pet.PetOwnerChanged;
import io.vlingo.developers.petclinic.model.client.ClientRegistered;
import io.vlingo.developers.petclinic.model.pet.PetRegistered;

@SuppressWarnings("rawtypes")
public class ProjectionDispatcherProvider {

  public final ProjectionDispatcher projectionDispatcher;
  public final Dispatcher storeDispatcher;

  public static ProjectionDispatcherProvider using(final Stage stage, QueryModelStateStoreProvider queryModelStateStoreProvider) {

    final List<ProjectToDescription> descriptions =
            Arrays.asList(
                    ProjectToDescription.with(SpecialtyTypeProjectionActor.class, Optional.of(queryModelStateStoreProvider.store), SpecialtyTypeOffered.class.getName(), SpecialtyTypeRenamed.class.getName()),
                    ProjectToDescription.with(AnimalTypeProjectionActor.class, Optional.of(queryModelStateStoreProvider.store), AnimalTypeRenamed.class.getName(), AnimalTypeTreatmentOffered.class.getName()),
                    ProjectToDescription.with(VeterinarianProjectionActor.class, Optional.of(queryModelStateStoreProvider.store), VeterinarianContactInformationChanged.class.getName(), VeterinarianSpecialtyChosen.class.getName(), VeterinarianRegistered.class.getName(), VeterinarianNameChanged.class.getName()),
                    ProjectToDescription.with(ClientProjectionActor.class, Optional.of(queryModelStateStoreProvider.store), ClientRegistered.class.getName(), ClientNameChanged.class.getName(), ClientContactInformationChanged.class.getName()),
                    ProjectToDescription.with(PetProjectionActor.class, Optional.of(queryModelStateStoreProvider.store), PetKindCorrected.class.getName(), PetNameChanged.class.getName(), PetRegistered.class.getName(), PetDeathRecorded.class.getName(), PetOwnerChanged.class.getName(), PetBirthRecorded.class.getName())
                    );

    final Protocols dispatcherProtocols =
            stage.actorFor(
                    new Class<?>[] { Dispatcher.class, ProjectionDispatcher.class },
                    Definition.has(TextProjectionDispatcherActor.class, Definition.parameters(descriptions)));

    final Protocols.Two<Dispatcher, ProjectionDispatcher> dispatchers = Protocols.two(dispatcherProtocols);

    return new ProjectionDispatcherProvider(dispatchers._1, dispatchers._2);
  }

  private ProjectionDispatcherProvider(final Dispatcher storeDispatcher, final ProjectionDispatcher projectionDispatcher) {
    this.storeDispatcher = storeDispatcher;
    this.projectionDispatcher = projectionDispatcher;
  }
}
