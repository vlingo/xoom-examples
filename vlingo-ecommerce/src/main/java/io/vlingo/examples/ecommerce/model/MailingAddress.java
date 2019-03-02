package io.vlingo.examples.ecommerce.model;

public class MailingAddress {

    public final String recipient;
    public final String addressLine1;
    public final String addressLine2;
    public final String cityName;
    public final String countryName;
    public final String zipCode;

    public MailingAddress(String recipient, String addressLine1, String addressLine2, String cityName, String countryName, String zipCode) {
        this.recipient = recipient;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.cityName = cityName;
        this.countryName = countryName;
        this.zipCode = zipCode;
    }

}
