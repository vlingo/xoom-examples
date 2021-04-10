package io.vlingo.xoom.examples.petclinic.model.veterinarian;

import io.vlingo.xoom.examples.petclinic.model.ContactInformation;
import io.vlingo.xoom.examples.petclinic.model.client.Specialty;
import io.vlingo.xoom.examples.petclinic.model.Fullname;

public final class VeterinarianState {

  public final String id;
  public final Fullname name;
  public final ContactInformation contact;
  public final Specialty specialty;

  public static VeterinarianState identifiedBy(final String id) {
    return new VeterinarianState(id, null, null, null);
  }

  public VeterinarianState (final String id, final Fullname name, final ContactInformation contact, final Specialty specialty) {
    this.id = id;
    this.name = name;
    this.contact = contact;
    this.specialty = specialty;
  }

  public VeterinarianState register(final Fullname name, final ContactInformation contact, final Specialty specialty) {
    return new VeterinarianState(this.id, name, contact, specialty);
  }

  public VeterinarianState changeName(final Fullname name) {
    return new VeterinarianState(this.id, name, this.contact, this.specialty);
  }

  public VeterinarianState changeContactInformation(final ContactInformation contact) {
    return new VeterinarianState(this.id, this.name, contact, this.specialty);
  }

  public VeterinarianState specializesIn(final Specialty specialty) {
    return new VeterinarianState(this.id, this.name, this.contact, specialty);
  }

}
