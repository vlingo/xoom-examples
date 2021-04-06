package io.vlingo.xoom.petclinic.model;

public final class ContactInformation {

  public final PostalAddress postalAddress;
  public final Telephone telephone;

  public static ContactInformation from(final PostalAddress postalAddress, final Telephone telephone) {
    return new ContactInformation(postalAddress, telephone);
  }

  private ContactInformation (final PostalAddress postalAddress, final Telephone telephone) {
    this.postalAddress = postalAddress;
    this.telephone = telephone;
  }

}