package io.vlingo.developers.petclinic.infrastructure;

import java.util.List;
import java.util.stream.Collectors;

import io.vlingo.developers.petclinic.model.veterinarian.VeterinarianState;

public class VeterinarianData {
  public final String id;
  public final FullnameData name;
  public final ContactInformationData contact;
  public final SpecialtyData specialty;

  public static VeterinarianData from(final VeterinarianState state) {
    return new VeterinarianData(state);
  }

  public static List<VeterinarianData> from(final List<VeterinarianState> states) {
    return states.stream().map(VeterinarianData::from).collect(Collectors.toList());
  }

  public static VeterinarianData from(final String id, final FullnameData name,
                                      final ContactInformationData contact, final SpecialtyData specialty){
    return new VeterinarianData(id, name, contact, specialty);
  }

  public static VeterinarianData empty() {
    return new VeterinarianData(VeterinarianState.identifiedBy(""));
  }

  private VeterinarianData (final VeterinarianState state) {
    this.id = state.id;
    this.name = FullnameData.of(state.name.first, state.name.last);
    this.contact = ContactInformationData.of(state.contact.postalAddress, state.contact.telephone);
    this.specialty = SpecialtyData.of(state.specialty.specialtyTypeId);
  }

  private VeterinarianData(String id, FullnameData name, ContactInformationData contact, SpecialtyData specialty) {
    this.id = id;
    this.name = name;
    this.contact = contact;
    this.specialty = specialty;
  }
}
