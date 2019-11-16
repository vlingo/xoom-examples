// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.saasovation.agilepm.model.product;

import com.saasovation.agilepm.model.Tenant;

public class ProductOwner {
  public final String id;
  public final Tenant tenant;
  
  public static ProductOwner fromExisting(String tenantId, final String id) {
    return new ProductOwner(Tenant.fromExisting(tenantId), id);
  }
  
  public static ProductOwner with(final Tenant tenant, final String id) {
    return new ProductOwner(tenant, id);
  }

  public ProductOwner(final Tenant tenant, final String id) {
    assert(tenant != null);
    assert(id != null);
    
    this.tenant = tenant;
    this.id = id;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((tenant == null) ? 0 : tenant.hashCode());
    return result;
  }

  @Override
  public boolean equals(final Object other) {
    if (this == other)
      return true;
    if (other == null || getClass() != other.getClass()) {
      return false;
    }
    
    final ProductOwner otherProductOwner = (ProductOwner) other;
    
    return tenant.equals(otherProductOwner.tenant) && id.equals(otherProductOwner.id);
  }

  @Override
  public String toString() {
    return "ProductOwner [tenant=" + tenant + ", id=" + id + "]";
  }
}
