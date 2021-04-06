package io.vlingo.xoom.petclinic.infrastructure.persistence;

import io.vlingo.xoom.annotation.persistence.Persistence;
import io.vlingo.xoom.annotation.persistence.Persistence.StorageType;
import io.vlingo.xoom.annotation.persistence.Projections;
import io.vlingo.xoom.annotation.persistence.Projection;
import io.vlingo.xoom.annotation.persistence.ProjectionType;
import io.vlingo.xoom.annotation.persistence.Adapters;
import io.vlingo.xoom.annotation.persistence.EnableQueries;
import io.vlingo.xoom.annotation.persistence.QueriesEntry;
import io.vlingo.xoom.annotation.persistence.DataObjects;
import io.vlingo.xoom.petclinic.model.animaltype.AnimalTypeRenamed;
import io.vlingo.xoom.petclinic.infrastructure.KindData;
import io.vlingo.xoom.petclinic.model.animaltype.AnimalTypeState;
import io.vlingo.xoom.petclinic.infrastructure.PetData;
import io.vlingo.xoom.petclinic.model.veterinarian.VeterinarianSpecialtyChosen;
import io.vlingo.xoom.petclinic.infrastructure.AnimalTypeData;
import io.vlingo.xoom.petclinic.infrastructure.OwnerData;
import io.vlingo.xoom.petclinic.model.pet.PetBirthRecorded;
import io.vlingo.xoom.petclinic.model.pet.PetDeathRecorded;
import io.vlingo.xoom.petclinic.model.pet.PetRegistered;
import io.vlingo.xoom.petclinic.infrastructure.DateData;
import io.vlingo.xoom.petclinic.infrastructure.NameData;
import io.vlingo.xoom.petclinic.model.specialtytype.SpecialtyTypeRenamed;
import io.vlingo.xoom.petclinic.model.pet.PetKindCorrected;
import io.vlingo.xoom.petclinic.model.pet.PetOwnerChanged;
import io.vlingo.xoom.petclinic.infrastructure.SpecialtyTypeData;
import io.vlingo.xoom.petclinic.infrastructure.TelephoneData;
import io.vlingo.xoom.petclinic.model.animaltype.AnimalTypeTreatmentOffered;
import io.vlingo.xoom.petclinic.model.veterinarian.VeterinarianNameChanged;
import io.vlingo.xoom.petclinic.model.specialtytype.SpecialtyTypeState;
import io.vlingo.xoom.petclinic.model.pet.PetState;
import io.vlingo.xoom.petclinic.model.client.ClientContactInformationChanged;
import io.vlingo.xoom.petclinic.model.specialtytype.SpecialtyTypeOffered;
import io.vlingo.xoom.petclinic.model.veterinarian.VeterinarianState;
import io.vlingo.xoom.petclinic.model.client.ClientRegistered;
import io.vlingo.xoom.petclinic.model.veterinarian.VeterinarianRegistered;
import io.vlingo.xoom.petclinic.infrastructure.PostalAddressData;
import io.vlingo.xoom.petclinic.model.client.ClientState;
import io.vlingo.xoom.petclinic.infrastructure.SpecialtyData;
import io.vlingo.xoom.petclinic.infrastructure.FullNameData;
import io.vlingo.xoom.petclinic.infrastructure.VisitData;
import io.vlingo.xoom.petclinic.infrastructure.VeterinarianData;
import io.vlingo.xoom.petclinic.model.veterinarian.VeterinarianContactInformationChanged;
import io.vlingo.xoom.petclinic.model.pet.PetNameChanged;
import io.vlingo.xoom.petclinic.infrastructure.ContactInformationData;
import io.vlingo.xoom.petclinic.infrastructure.ClientData;
import io.vlingo.xoom.petclinic.model.client.ClientNameChanged;

@Persistence(basePackage = "io.vlingo.xoom.petclinic", storageType = StorageType.JOURNAL, cqrs = true)
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