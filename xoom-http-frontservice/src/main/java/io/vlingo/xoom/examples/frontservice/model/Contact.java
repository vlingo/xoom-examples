// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.frontservice.model;

public final class Contact {
  public final String emailAddress;
  public final String telephoneNumber;

  public static Contact from(final String emailAddress, final String telephoneNumber) {
    return new Contact(emailAddress, telephoneNumber);
  }

  public Contact(final String emailAddress, final String telephoneNumber) {
    this.emailAddress = emailAddress;
    this.telephoneNumber = telephoneNumber;
  }

  @Override
  public String toString() {
    return "Contact[emailAddress=" + emailAddress + ", telephoneNumber=" + telephoneNumber + "]";
  }
}
