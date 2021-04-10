package io.vlingo.xoom.examples.petclinic.infrastructure.resource;

import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.turbo.annotation.autodispatch.Handler.Three;
import io.vlingo.xoom.turbo.annotation.autodispatch.Handler.Two;
import io.vlingo.xoom.turbo.annotation.autodispatch.HandlerEntry;

import io.vlingo.xoom.examples.petclinic.model.veterinarian.VeterinarianState;
import io.vlingo.xoom.examples.petclinic.model.*;
import io.vlingo.xoom.examples.petclinic.infrastructure.persistence.VeterinarianQueries;
import io.vlingo.xoom.examples.petclinic.model.veterinarian.Veterinarian;
import io.vlingo.xoom.examples.petclinic.infrastructure.VeterinarianData;
import java.util.Collection;

public class VeterinarianResourceHandlers {

  public static final int REGISTER = 0;
  public static final int CHANGE_CONTACT_INFORMATION = 1;
  public static final int CHANGE_NAME = 2;
  public static final int SPECIALIZES_IN = 3;
  public static final int VETERINARIANS = 4;
  public static final int ADAPT_STATE = 5;

  public static final HandlerEntry<Three<Completes<VeterinarianState>, Stage, VeterinarianData>> REGISTER_HANDLER =
          HandlerEntry.of(REGISTER, ($stage, data) -> {
              final FullName name = FullName.from(data.name.first, data.name.last);
              final PostalAddress postalAddress = PostalAddress.from(data.contactInformation.postalAddress.streetAddress, data.contactInformation.postalAddress.city, data.contactInformation.postalAddress.stateProvince, data.contactInformation.postalAddress.postalCode);
              final Telephone telephone = Telephone.from(data.contactInformation.telephone.number);
              final ContactInformation contactInformation = ContactInformation.from(postalAddress, telephone);
              final Specialty specialty = Specialty.from(data.specialty.specialtyTypeId);
              return Veterinarian.register($stage, name, contactInformation, specialty);
          });

  public static final HandlerEntry<Three<Completes<VeterinarianState>, Veterinarian, VeterinarianData>> CHANGE_CONTACT_INFORMATION_HANDLER =
          HandlerEntry.of(CHANGE_CONTACT_INFORMATION, (veterinarian, data) -> {
              final PostalAddress postalAddress = PostalAddress.from(data.contactInformation.postalAddress.streetAddress, data.contactInformation.postalAddress.city, data.contactInformation.postalAddress.stateProvince, data.contactInformation.postalAddress.postalCode);
              final Telephone telephone = Telephone.from(data.contactInformation.telephone.number);
              final ContactInformation contactInformation = ContactInformation.from(postalAddress, telephone);
              return veterinarian.changeContactInformation(contactInformation);
          });

  public static final HandlerEntry<Three<Completes<VeterinarianState>, Veterinarian, VeterinarianData>> CHANGE_NAME_HANDLER =
          HandlerEntry.of(CHANGE_NAME, (veterinarian, data) -> {
              final FullName name = FullName.from(data.name.first, data.name.last);
              return veterinarian.changeName(name);
          });

  public static final HandlerEntry<Three<Completes<VeterinarianState>, Veterinarian, VeterinarianData>> SPECIALIZES_IN_HANDLER =
          HandlerEntry.of(SPECIALIZES_IN, (veterinarian, data) -> {
              final Specialty specialty = Specialty.from(data.specialty.specialtyTypeId);
              return veterinarian.specializesIn(specialty);
          });

  public static final HandlerEntry<Two<VeterinarianData, VeterinarianState>> ADAPT_STATE_HANDLER =
          HandlerEntry.of(ADAPT_STATE, VeterinarianData::from);

  public static final HandlerEntry<Two<Completes<Collection<VeterinarianData>>, VeterinarianQueries>> QUERY_ALL_HANDLER =
          HandlerEntry.of(VETERINARIANS, VeterinarianQueries::veterinarians);

}