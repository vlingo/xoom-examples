package io.examples.account.domain.model;

/**
 * This is a simple {@link AccountAddress} value object and entity.
 *
 * @author Kenny Bastani
 * @see AccountQuery
 */
public class AccountAddress {

    private String street1;
    private String street2;
    private String state;
    private String city;
    private String country;
    private Integer zipCode;
    private AddressType type;

    public AccountAddress() {
    }

    /**
     * Instantiates a new {@link AccountAddress} entity with overloaded arguments.
     */
    public AccountAddress(String street1, String street2, String state, String city, String country, AddressType type,
                          Integer zipCode) {
        this.street1 = street1;
        this.street2 = street2;
        this.state = state;
        this.city = city;
        this.country = country;
        this.type = type;
        this.zipCode = zipCode;
    }

    public String getStreet1() {
        return street1;
    }

    public void setStreet1(String street1) {
        this.street1 = street1;
    }

    public String getStreet2() {
        return street2;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getZipCode() {
        return zipCode;
    }

    public void setZipCode(Integer zipCode) {
        this.zipCode = zipCode;
    }

    public AddressType getType() {
        return type;
    }

    public void setType(AddressType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Address{" +
                "street1='" + street1 + '\'' +
                ", street2='" + street2 + '\'' +
                ", state='" + state + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", zipCode=" + zipCode +
                ", type=" + type +
                "} " + super.toString();
    }

    /**
     * The {@link AddressType} represents the type of address that is assigned to an {@link AccountQuery}.
     *
     * @author Kenny Bastani
     * @see AccountQuery
     * @see AccountAddress
     */
    public enum AddressType {
        SHIPPING,
        BILLING
    }
}
