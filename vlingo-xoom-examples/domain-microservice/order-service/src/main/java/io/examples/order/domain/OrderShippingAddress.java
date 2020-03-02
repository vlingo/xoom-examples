package io.examples.order.domain;

import com.google.gson.Gson;
import io.examples.account.domain.model.AccountAddress;
import io.examples.account.domain.model.AccountQuery;
import io.examples.infra.Identity;

import javax.persistence.Entity;

/**
 * This is a simple {@link OrderShippingAddress} value object and entity.
 *
 * @author Kenny Bastani
 * @see AccountQuery
 */
@Entity
public class OrderShippingAddress extends Identity {

    private String street1;
    private String street2;
    private String state;
    private String city;
    private String country;
    private Integer zipCode;
    private AddressType type;

    public OrderShippingAddress() {
    }

    public OrderShippingAddress(String street1, String street2, String state, String city, String country, int zipCode) {
        this(street1, street2, state, city, country, AddressType.SHIPPING, zipCode);
    }

    public static OrderShippingAddress translateFrom(AccountAddress accountAddress) {
        // Context map from an account address to an order address using reverse JSON serialization
        Gson gson = new Gson();
        return gson.fromJson(gson.toJson(accountAddress), OrderShippingAddress.class);
    }

    /**
     * Instantiates a new {@link OrderShippingAddress} entity with overloaded arguments.
     */
    public OrderShippingAddress(String street1, String street2, String state, String city, String country, AddressType type,
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
     * @see OrderShippingAddress
     */
    public enum AddressType {
        SHIPPING,
        BILLING
    }
}
