package io.vlingo.xoom.examples.petclinic.model.veterinarian;

import io.vlingo.xoom.examples.petclinic.model.*;

public final class VeterinarianState {

  public final String id;
  public final FullName name;
  public final ContactInformation contactInformation;
  public final Specialty specialty;

  public static VeterinarianState identifiedBy(final String id) {
    return new VeterinarianState(id, null, null, null);
  }

  public VeterinarianState (final String id, final FullName name, final ContactInformation contactInformation, final Specialty specialty) {
    this.id = id;
    this.name = name;
    this.contactInformation = contactInformation;
    this.specialty = specialty;
  }

  public VeterinarianState register(final FullName name, final ContactInformation contactInformation, final Specialty specialty) {
    return new VeterinarianState(this.id, name, contactInformation, specialty);
  }

  public VeterinarianState changeContactInformation(final ContactInformation contactInformation) {
    return new VeterinarianState(this.id, this.name, contactInformation, this.specialty);
  }

  public VeterinarianState changeName(final FullName name) {
    return new VeterinarianState(this.id, name, this.contactInformation, this.specialty);
  }

  public VeterinarianState specializesIn(final Specialty specialty) {
    return new VeterinarianState(this.id, this.name, this.contactInformation, specialty);
  }

}
