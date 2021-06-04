package io.vlingo.xoom.examples.petclinic.infrastructure.persistence;

import io.vlingo.xoom.turbo.annotation.persistence.Persistence;
import io.vlingo.xoom.turbo.annotation.persistence.Persistence.StorageType;
import io.vlingo.xoom.turbo.annotation.persistence.Projections;
import io.vlingo.xoom.turbo.annotation.persistence.Projection;
import io.vlingo.xoom.turbo.annotation.persistence.ProjectionType;
import io.vlingo.xoom.turbo.annotation.persistence.Adapters;
import io.vlingo.xoom.turbo.annotation.persistence.EnableQueries;
import io.vlingo.xoom.turbo.annotation.persistence.QueriesEntry;
import io.vlingo.xoom.turbo.annotation.persistence.DataObjects;
import io.vlingo.xoom.examples.petclinic.model.animaltype.AnimalTypeRenamed;
import io.vlingo.xoom.examples.petclinic.infrastructure.KindData;
import io.vlingo.xoom.examples.petclinic.model.animaltype.AnimalTypeState;
import io.vlingo.xoom.examples.petclinic.infrastructure.PetData;
import io.vlingo.xoom.examples.petclinic.model.veterinarian.VeterinarianSpecialtyChosen;
import io.vlingo.xoom.examples.petclinic.infrastructure.AnimalTypeData;
import io.vlingo.xoom.examples.petclinic.infrastructure.OwnerData;
import io.vlingo.xoom.examples.petclinic.model.pet.PetBirthRecorded;
import io.vlingo.xoom.examples.petclinic.model.pet.PetDeathRecorded;
import io.vlingo.xoom.examples.petclinic.model.pet.PetRegistered;
import io.vlingo.xoom.examples.petclinic.infrastructure.DateData;
import io.vlingo.xoom.examples.petclinic.infrastructure.NameData;
import io.vlingo.xoom.examples.petclinic.model.specialtytype.SpecialtyTypeRenamed;
import io.vlingo.xoom.examples.petclinic.model.pet.PetKindCorrected;
import io.vlingo.xoom.examples.petclinic.model.pet.PetOwnerChanged;
import io.vlingo.xoom.examples.petclinic.infrastructure.SpecialtyTypeData;
import io.vlingo.xoom.examples.petclinic.infrastructure.TelephoneData;
import io.vlingo.xoom.examples.petclinic.model.animaltype.AnimalTypeTreatmentOffered;
import io.vlingo.xoom.examples.petclinic.model.veterinarian.VeterinarianNameChanged;
import io.vlingo.xoom.examples.petclinic.model.specialtytype.SpecialtyTypeState;
import io.vlingo.xoom.examples.petclinic.model.pet.PetState;
import io.vlingo.xoom.examples.petclinic.model.client.ClientContactInformationChanged;
import io.vlingo.xoom.examples.petclinic.model.specialtytype.SpecialtyTypeOffered;
import io.vlingo.xoom.examples.petclinic.model.veterinarian.VeterinarianState;
import io.vlingo.xoom.examples.petclinic.model.client.ClientRegistered;
import io.vlingo.xoom.examples.petclinic.model.veterinarian.VeterinarianRegistered;
import io.vlingo.xoom.examples.petclinic.infrastructure.PostalAddressData;
import io.vlingo.xoom.examples.petclinic.model.client.ClientState;
import io.vlingo.xoom.examples.petclinic.infrastructure.SpecialtyData;
import io.vlingo.xoom.examples.petclinic.infrastructure.FullNameData;
import io.vlingo.xoom.examples.petclinic.infrastructure.VisitData;
import io.vlingo.xoom.examples.petclinic.infrastructure.VeterinarianData;
import io.vlingo.xoom.examples.petclinic.model.veterinarian.VeterinarianContactInformationChanged;
import io.vlingo.xoom.examples.petclinic.model.pet.PetNameChanged;
import io.vlingo.xoom.examples.petclinic.infrastructure.ContactInformationData;
import io.vlingo.xoom.examples.petclinic.infrastructure.ClientData;
import io.vlingo.xoom.examples.petclinic.model.client.ClientNameChanged;

@Persistence(basePackage = "io.vlingo.xoom.examples.petclinic", storageType = StorageType.JOURNAL, cqrs = true)
@Projections(value = {
  @Projection(actor = AnimalTypeProjectionActor.class, becauseOf = {AnimalTypeRenamed.class, AnimalTypeTreatmentOffered.class}),
  @Projection(actor = SpecialtyTypeProjectionActor.class, becauseOf = {SpecialtyTypeOffered.class, SpecialtyTypeRenamed.class}),
  @Projection(actor = ClientProjectionActor.class, becauseOf = {ClientRegistered.class, ClientNameChanged.class, ClientContactInformationChanged.class}),
  @Projection(actor = VeterinarianProjectionActor.class, becauseOf = {VeterinarianContactInformationChanged.class, VeterinarianSpecialtyChosen.class, VeterinarianRegistered.class, VeterinarianNameChanged.class}),
  @Projection(actor = PetProjectionActor.class, becauseOf = {PetKindCorrected.class, PetNameChanged.class, PetRegistered.class, PetDeathRecorded.class, PetOwnerChanged.class, PetBirthRecorded.class})
}, type = ProjectionType.EVENT_BASED)
@Adapters({
  AnimalTypeRenamed.class,
  VeterinarianSpecialtyChosen.class,
  PetKindCorrected.class,
  PetRegistered.class,
  VeterinarianRegistered.class,
  PetOwnerChanged.class,
  ClientContactInformationChanged.class,
  VeterinarianNameChanged.class,
  SpecialtyTypeOffered.class,
  SpecialtyTypeRenamed.class,
  VeterinarianContactInformationChanged.class,
  PetNameChanged.class,
  ClientRegistered.class,
  ClientNameChanged.class,
  PetDeathRecorded.class,
  PetBirthRecorded.class,
  AnimalTypeTreatmentOffered.class
})
@EnableQueries({
  @QueriesEntry(protocol = SpecialtyTypeQueries.class, actor = SpecialtyTypeQueriesActor.class),
  @QueriesEntry(protocol = AnimalTypeQueries.class, actor = AnimalTypeQueriesActor.class),
  @QueriesEntry(protocol = VeterinarianQueries.class, actor = VeterinarianQueriesActor.class),
  @QueriesEntry(protocol = ClientQueries.class, actor = ClientQueriesActor.class),
  @QueriesEntry(protocol = PetQueries.class, actor = PetQueriesActor.class),
})
@DataObjects({VisitData.class, TelephoneData.class, KindData.class, ContactInformationData.class, PetData.class, SpecialtyTypeData.class, VeterinarianData.class, OwnerData.class, AnimalTypeData.class, ClientData.class, PostalAddressData.class, DateData.class, SpecialtyData.class, NameData.class, FullNameData.class})
public class PersistenceSetup {


}