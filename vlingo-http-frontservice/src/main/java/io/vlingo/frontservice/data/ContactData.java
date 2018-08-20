// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.frontservice.data;

import io.vlingo.frontservice.model.Contact;

public class ContactData {
  public final String emailAddress;
  public final String telephoneNumber;

  public static ContactData from(final String emailAddress, final String telephoneNumber) {
    return new ContactData(emailAddress, telephoneNumber);
  }

  public static ContactData from(final Contact contact) {
    return new ContactData(contact.emailAddress, contact.telephoneNumber);
  }

  public ContactData(final String emailAddress, final String telephoneNumber) {
    this.emailAddress = emailAddress;
    this.telephoneNumber = telephoneNumber;
  }

  @Override
  public String toString() {
    return "ContactData[emailAddress=" + emailAddress + ", telephoneNumber=" + telephoneNumber + "]";
  }
}
