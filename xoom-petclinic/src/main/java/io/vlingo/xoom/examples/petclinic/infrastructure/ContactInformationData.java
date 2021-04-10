package io.vlingo.xoom.examples.petclinic.infrastructure;

import io.vlingo.xoom.examples.petclinic.model.PostalAddress;
import io.vlingo.xoom.examples.petclinic.model.Telephone;

public class ContactInformationData {

  public final PostalAddress postalAddress;
  public final Telephone telephone;

  public static ContactInformationData of(final PostalAddress postalAddress, final Telephone telephone) {
    return new ContactInformationData(postalAddress, telephone);
  }

  private ContactInformationData (final PostalAddress postalAddress, final Telephone telephone) {
    this.postalAddress = postalAddress;
    this.telephone = telephone;
  }

}
