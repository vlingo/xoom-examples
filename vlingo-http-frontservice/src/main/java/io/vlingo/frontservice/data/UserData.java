// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.frontservice.data;

import java.util.List;
import java.util.UUID;

import io.vlingo.frontservice.model.User;

public class UserData {
  public final String id;
  public final NameData nameData;
  public final ContactData contactData;
  public final String publicSecurityToken;

  public static UserData empty() {
    return new UserData(null, null, null, null);
  }

  public static UserData from(final NameData nameData, final ContactData contactData, final String publicSecurityToken) {
    return new UserData(nameData, contactData, publicSecurityToken);
  }
  
  public static UserData from(final String id, final NameData nameData, final ContactData contactData, final String publicSecurityToken) {
    return new UserData(id, nameData, contactData, publicSecurityToken);
  }
  
  public static UserData from(final User.UserState userState) {
    return new UserData(userState.id, NameData.from(userState.name), ContactData.from(userState.contact), userState.security.publicToken);
  }

  public static UserData userAt(final String location, List<UserData> userData) {
    final int index = location.lastIndexOf("/");
    final String id = location.substring(index+1);
    return userOf(id, userData);
  }

  public static UserData userOf(final String id, List<UserData> userData) {
    for (final UserData data : userData) {
      if (data.id.equals(id)) {
        return data;
      }
    }
    return null;
  }

  public UserData(final NameData nameData, final ContactData contactData, final String publicSecurityToken) {
    this(UUID.randomUUID().toString(), nameData, contactData, publicSecurityToken);
  }
  
  public UserData(final String id, final NameData nameData, final ContactData contactData, final String publicSecurityToken) {
    this.id = id;
    this.nameData = nameData;
    this.contactData = contactData;
    this.publicSecurityToken = publicSecurityToken;
  }

  public boolean doesNotExist() {
    return id == null;
  }

  @Override
  public String toString() {
    return "UserData[id=" + id + ", nameData=" + nameData + ", contactData=" + contactData + ", publicSecurityToken=" + publicSecurityToken + "]";
  }
}
