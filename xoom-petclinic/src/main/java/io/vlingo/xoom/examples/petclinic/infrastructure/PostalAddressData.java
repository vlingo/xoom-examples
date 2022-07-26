package io.vlingo.xoom.examples.petclinic.infrastructure;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vlingo.xoom.examples.petclinic.model.PostalAddress;

public class PostalAddressData {

  public final String streetAddress;
  public final String city;
  public final String stateProvince;
  public final String postalCode;

  public static PostalAddressData from(final PostalAddress postalAddress) {
    return from(postalAddress.streetAddress, postalAddress.city, postalAddress.stateProvince, postalAddress.postalCode);
  }

  @JsonCreator
  public static PostalAddressData from(@JsonProperty("streetAddress") final String streetAddress,
                                       @JsonProperty("city") final String city,
                                       @JsonProperty("stateProvince") final String stateProvince,
                                       @JsonProperty("postalCode") final String postalCode) {
    return new PostalAddressData(streetAddress, city, stateProvince, postalCode);
  }

  private PostalAddressData (final String streetAddress, final String city, final String stateProvince, final String postalCode) {
    this.streetAddress = streetAddress;
    this.city = city;
    this.stateProvince = stateProvince;
    this.postalCode = postalCode;
  }

}