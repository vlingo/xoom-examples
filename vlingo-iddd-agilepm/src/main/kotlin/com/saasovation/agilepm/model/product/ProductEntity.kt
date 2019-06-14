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
    fun define(val stage: Stage, val tenant: Tenant, val productOwner: ProductOwner, val name: String, val description: String): Product {
      val product = stage.actorFor(Product::class.java, ProductEntity::class.java)
      product.define(tenant, productOwner, name, description)
      return product
    }
  }

  fun changeProductOwner(val productOwner: ProductOwner)
  fun define(val tenant: Tenant, val productOwner: ProductOwner, val name: String, val description: String)
}
