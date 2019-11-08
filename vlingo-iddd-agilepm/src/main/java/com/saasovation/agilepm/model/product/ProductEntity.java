// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.saasovation.agilepm.model.product;

import com.saasovation.agilepm.model.Tenant;
import com.saasovation.agilepm.model.product.Events.ProductDefined;
import com.saasovation.agilepm.model.product.Events.ProductDescriptionChanged;
import com.saasovation.agilepm.model.product.Events.ProductDiscussionAttached;
import com.saasovation.agilepm.model.product.Events.ProductDiscussionRequested;
import com.saasovation.agilepm.model.product.Events.ProductNameChanged;
import com.saasovation.agilepm.model.product.Events.ProductOwnerAssigned;

import io.vlingo.lattice.model.sourcing.EventSourced;

public class ProductEntity extends EventSourced implements Product {
  private State state;
  
  public ProductEntity(final Tenant tenant, final ProductId productId) {
    this.state = State.inital(tenant, productId);
  }

  public void assignProductOwner(final ProductOwner productOwner) {
    if (productOwner != null && !state.productOwner.equals(productOwner)) {
      apply(ProductOwnerAssigned.with(state.tenant, state.productId, productOwner));
    }
  }
  
  public void attachDiscussion(final String discussionId) {
    if (state.hasDiscussion && !state.discussionId.equals(discussionId)) {
      apply(ProductDiscussionAttached.with(state.tenant, state.productId, discussionId));
    }
  }

  public void changeDescription(final String description) {
    if (!state.description.equals(description)) {
      apply(ProductDescriptionChanged.with(state.tenant, state.productId, description));
    }
  }

  public void changeName(final String name) {
    if (!state.name.equals(name)) {
      apply(ProductNameChanged.with(state.tenant, state.productId, name));
    }
  }

  public void define(final ProductOwner productOwner, final String name, final String description, final boolean hasDiscussion) {
    if (state.productOwner == null) {
      apply(ProductDefined.with(state.tenant, state.productId, productOwner, name, description, hasDiscussion));
    }
  }

  public void requestDiscussion() {
    if (!state.hasDiscussion) {
      apply(ProductDiscussionRequested.with(state.tenant, state.productId));
    }
  }

  @Override
  protected String streamName() {
    return state.tenant.id + ":" + state.productId.id;
  }
}
