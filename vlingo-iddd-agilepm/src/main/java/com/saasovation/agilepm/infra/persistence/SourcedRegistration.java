// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.saasovation.agilepm.infra.persistence;

import com.saasovation.agilepm.model.product.ProductEntity;

import io.vlingo.lattice.model.sourcing.SourcedTypeRegistry;
import io.vlingo.lattice.model.sourcing.SourcedTypeRegistry.Info;
import io.vlingo.symbio.store.journal.Journal;

public class SourcedRegistration {
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public static <T> void registerAllWith(final SourcedTypeRegistry registry, final Journal<T> journal) {
    registry.register(new Info(journal, ProductEntity.class, ProductEntity.class.getSimpleName()));
  }
}
