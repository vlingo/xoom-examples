// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.saasovation.agilepm.model.product

import io.vlingo.actors.Stage

public interface Product {
  companion object {
    fun define(val stage: Stage, val tenant: Tenant, val productOwner: ProductOwner, val name: String, val description: String): Pair<ProductId, Product> {
      val product = stage.actorFor(Product::class.java, ProductEntity::class.java)
      val productId = ProdcutId(java.util.UUID.randomUUID().toString)
      product.define(tenant, productId, productOwner, name, description)
      return Pair(productId, product)
    }

    data class ProductId(val id: String)
    data class ProductDefined(val tenant: Tenant, val productId: ProductId, val productOwner: ProductOwner, val name: String, val description: String) : io.vlingo.lattice.model.DomainEvent
    data class ProductOwnerChanged(val tenant: Tenant, productId: ProductId, val productOwner: ProductOwner)
  }

  fun changeProductOwner(val productOwner: ProductOwner)
  fun define(val tenant: Tenant, val productOwner: ProductOwner, val name: String, val description: String)

  data class State(val tenant: Tenant, val productId: ProductId, val productOwner: ProductOwner, val name: String, val description: String)
}
