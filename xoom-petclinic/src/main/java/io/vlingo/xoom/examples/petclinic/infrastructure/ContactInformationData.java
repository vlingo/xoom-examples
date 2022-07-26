package io.vlingo.xoom.examples.petclinic.infrastructure;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vlingo.xoom.examples.petclinic.model.ContactInformation;

public class ContactInformationData {

  public final PostalAddressData postalAddress;
  public final TelephoneData telephone;

  public static ContactInformationData from(final ContactInformation contactInformation) {
    final PostalAddressData postalAddress = contactInformation.postalAddress != null ? PostalAddressData.from(contactInformation.postalAddress) : null;
    final TelephoneData telephone = contactInformation.telephone != null ? TelephoneData.from(contactInformation.telephone) : null;
    return from(postalAddress, telephone);
  }

  @JsonCreator
  public static ContactInformationData from(@JsonProperty("postalAddress") final PostalAddressData postalAddress,
                                            @JsonProperty("telephone") final TelephoneData telephone) {
    return new ContactInformationData(postalAddress, telephone);
  }

  private ContactInformationData (final PostalAddressData postalAddress, final TelephoneData telephone) {
    this.postalAddress = postalAddress;
    this.telephone = telephone;
  }

}