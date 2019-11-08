// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.saasovation.agilepm.model.product

import io.vlingo.actors.Stage
import io.vlingo.lattice.model.DomainEvent

public class ProductEntity : Product, Sourced<DomainEvent> {
  var State state

  fun changeProductOwner(val productOwner: ProductOwner): Completes<State> {
    apply(ProductOwnerChanged(state.tenant.id, state.productId.id, productOwner.id))
  }

  fun define(val tenant: Tenant, val productId: ProductId, val productOwner: ProductOwner, val name: String, val description: String) {
    apply(ProductDefined())
  }
}
