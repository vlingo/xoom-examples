// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.frontservice.resource;

import io.vlingo.http.resource.DefaultJsonMapper;
import io.vlingo.http.resource.Mapper;

/**
 * ProfileDataMapper is a custom ResourceMapper, but as you can see,
 * it just delegates the DefaultResourceMapper. This is meant only
 * to demonstrate that if you need a custom ResourceMapper, you can
 * create one and wire it in the vlingo-http.properties file with
 * the action for which it maps resources:
 * <p>{@code action.RESOURCE_NAME.ACTION_NAME.mapper = fully.qualified.classname }</p>
 * <p>As in:</p>
 * <p>{@code action.profile.define.mapper = io.vlingo.http.sample.user.ProfileDataMapper }</p>
 */
public class ProfileDataMapper implements Mapper {

  @Override
  public <T> T from(final String data, final Class<T> type) {
    return DefaultJsonMapper.instance.from(data, type);
  }

  @Override
  public <T> String from(final T data) {
    return DefaultJsonMapper.instance.from(data);
  }
}
