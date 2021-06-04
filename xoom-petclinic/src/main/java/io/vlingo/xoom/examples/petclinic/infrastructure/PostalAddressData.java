package io.vlingo.xoom.examples.petclinic.infrastructure;

import io.vlingo.xoom.examples.petclinic.model.PostalAddress;

public class PostalAddressData {

  public final String streetAddress;
  public final String city;
  public final String stateProvince;
  public final String postalCode;

  public static PostalAddressData from(final PostalAddress postalAddress) {
    return from(postalAddress.streetAddress, postalAddress.city, postalAddress.stateProvince, postalAddress.postalCode);
  }

  public static PostalAddressData from(final String streetAddress, final String city, final String stateProvince, final String postalCode) {
    return new PostalAddressData(streetAddress, city, stateProvince, postalCode);
  }

  private PostalAddressData (final String streetAddress, final String city, final String stateProvince, final String postalCode) {
    this.streetAddress = streetAddress;
    this.city = city;
    this.stateProvince = stateProvince;
    this.postalCode = postalCode;
  }

}