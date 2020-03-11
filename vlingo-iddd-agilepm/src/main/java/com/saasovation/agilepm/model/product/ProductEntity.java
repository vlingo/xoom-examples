// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.saasovation.agilepm.model.product;

import com.saasovation.agilepm.model.Tenant;
import com.saasovation.agilepm.model.product.Events.*;
import io.vlingo.common.Completes;
import io.vlingo.lattice.model.sourcing.EventSourced;

public class ProductEntity extends EventSourced implements Product {
    private State state;

    static {
        EventSourced.registerConsumer(ProductEntity.class, ProductDefined.class, ProductEntity::applyProductDefined);
        EventSourced.registerConsumer(ProductEntity.class, ProductDiscussionAttached.class, ProductEntity::applyProductDiscussionAttached);
        EventSourced.registerConsumer(ProductEntity.class, ProductDiscussionRequested.class, ProductEntity::applyProductDiscussionRequested);
    }

    public ProductEntity(final Tenant tenant, final ProductId productId) {
        super(productId.id);
        this.state = State.inital(tenant, productId);
    }

    public void assignProductOwner(final ProductOwner productOwner) {
        if (productOwner != null && !state.productOwner.equals(productOwner)) {
            apply(ProductOwnerAssigned.with(state.tenant, state.productId, productOwner));
        }
    }

    public void attachDiscussion(final String discussionId) {
        if (state.hasDiscussion && (state.discussionId == null || !state.discussionId.equals(discussionId))) {
            apply(ProductDiscussionAttached.with(state.tenant, state.productId, discussionId));
        }
    }

    public Completes<String> changeDescription(final String description) {
        if (!state.description.equals(description)) {
            apply(ProductDescriptionChanged.with(state.tenant, state.productId, description), () -> description);
        }
        return completes().with(description);
    }

    public void changeName(final String name) {
        if (!state.name.equals(name)) {
            apply(ProductNameChanged.with(state.tenant, state.productId, name));
        }
    }

    @SuppressWarnings("unchecked")
    public void define(final ProductOwner productOwner, final String name, final String description, final boolean hasDiscussion) {
        if (state.productOwner == null) {
          if (hasDiscussion) {
            logger().debug("apply(): Product id:" + state.productId.id);
            apply(asList(ProductDefined.with(state.tenant, state.productId, productOwner, name, description, hasDiscussion),
                         ProductDiscussionRequested.with(state.tenant, state.productId, productOwner, name, description)));
          } else {
            apply(ProductDefined.with(state.tenant, state.productId, productOwner, name, description, hasDiscussion));
          }
        }
    }

    public void requestDiscussion() {
        if (!state.hasDiscussion) {
            apply(ProductDiscussionRequested.with(state.tenant, state.productId, state.productOwner, state.name, state.description));
        }
    }

    @Override
    public Completes<State> query() {
      return completes().with(state);
    }

    private void applyProductDefined(final ProductDefined e) {
        logger().info("Product defined");
        state = new State(Tenant.fromExisting(e.tenantId),
                ProductId.fromExisting(e.productId),
                ProductOwner.fromExisting(e.tenantId, e.productOwnerId),
                e.name, e.description, e.hasDiscussion);
    }

    private void applyProductDiscussionRequested(final ProductDiscussionRequested e) {
        logger().info("Product discussion requested");
        state = state.withDiscussionRequested();
    }

    private void applyProductDiscussionAttached(final ProductDiscussionAttached e) {
        logger().info("Product discussion attached");
        state = state.withDiscussion(e.discussionId);
    }
}
