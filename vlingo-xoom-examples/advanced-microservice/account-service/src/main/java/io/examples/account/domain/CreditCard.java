package io.examples.account.domain;

import io.examples.account.data.Identity;

import javax.persistence.*;

/**
 * A {@link CreditCard} belonging to a customer {@link Account} is used for processing payments.
 *
 * @author Kenny Bastani
 * @see Account
 * @see CreditCardType
 */
@Entity
public class CreditCard extends Identity {

    private String number;
    private CreditCardType type;

    public CreditCard() {
    }

    public CreditCard(String number, CreditCardType type) {
        this.number = number;
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public CreditCardType getType() {
        return type;
    }

    public void setType(CreditCardType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "CreditCard{" +
                "number='" + number + '\'' +
                ", type=" + type +
                "} " + super.toString();
    }

    /**
     * The {@link CreditCardType} is used to categorized the provider of a {@link CreditCard} for processing payments.
     *
     * @author Kenny Bastani
     * @see CreditCard
     */
    public enum CreditCardType {
        VISA,
        MASTERCARD,
        AMERICAN_EXPRESS
    }
}
