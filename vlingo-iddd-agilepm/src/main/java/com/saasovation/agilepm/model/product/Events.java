// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.saasovation.agilepm.model.product;

import com.saasovation.agilepm.model.Tenant;
import io.vlingo.lattice.model.DomainEvent;

public class Events {

    public static class ProductDefined extends DomainEvent {
        public final String description;
        public final boolean hasDiscussion;
        public final String name;
        public final String productId;
        public final String productOwnerId;
        public final String tenantId;

        public static ProductDefined with(final Tenant tenant, final ProductId productId, final ProductOwner productOwner, final String name, final String description, final boolean hasDiscussion) {
            return new ProductDefined(tenant, productId, productOwner, name, description, hasDiscussion);
        }

        public ProductDefined(final Tenant tenant, final ProductId productId, final ProductOwner productOwner, final String name, final String description, final boolean hasDiscussion) {
            this.tenantId = tenant.id;
            this.productId = productId.id;
            this.productOwnerId = productOwner.id;
            this.name = name;
            this.description = description;
            this.hasDiscussion = hasDiscussion;
        }
    }

    public static class ProductDiscussionAttached extends DomainEvent {
        public final String discussionId;
        public final String productId;
        public final String tenantId;

        public static ProductDiscussionAttached with(final Tenant tenant, final ProductId productId, final String discussionId) {
            return new ProductDiscussionAttached(tenant, productId, discussionId);
        }

        public ProductDiscussionAttached(final Tenant tenant, final ProductId productId, final String discussionId) {
            this.tenantId = tenant.id;
            this.productId = productId.id;
            this.discussionId = discussionId;
        }
    }

    public static class ProductDescriptionChanged extends DomainEvent {
        public final String description;
        public final String productId;
        public final String tenantId;

        public static ProductDescriptionChanged with(final Tenant tenant, final ProductId productId, final String description) {
            return new ProductDescriptionChanged(tenant, productId, description);
        }

        public ProductDescriptionChanged(final Tenant tenant, final ProductId productId, final String description) {
            this.tenantId = tenant.id;
            this.productId = productId.id;
            this.description = description;
        }
    }

    public static class ProductNameChanged extends DomainEvent {
        public final String name;
        public final String productId;
        public final String tenantId;

        public static ProductNameChanged with(final Tenant tenant, final ProductId productId, final String name) {
            return new ProductNameChanged(tenant, productId, name);
        }

        public ProductNameChanged(final Tenant tenant, final ProductId productId, final String name) {
            this.tenantId = tenant.id;
            this.productId = productId.id;
            this.name = name;
        }
    }

    public static class ProductOwnerAssigned extends DomainEvent {
        public final String productOwnerId;
        public final String productId;
        public final String tenantId;

        public static ProductOwnerAssigned with(final Tenant tenant, final ProductId productId, final ProductOwner productOwner) {
            return new ProductOwnerAssigned(tenant, productId, productOwner);
        }

        public ProductOwnerAssigned(final Tenant tenant, final ProductId productId, final ProductOwner productOwner) {
            this.tenantId = tenant.id;
            this.productId = productId.id;
            this.productOwnerId = productOwner.id;
        }
    }

    public static class ProductDiscussionRequested extends DomainEvent {
        public final String ownerId;
        public final String productId;
        public final String tenantId;
        public final String productOwnerId;
        public final String description;
        public final String name;

        public static ProductDiscussionRequested with(final Tenant tenant, final ProductId productId, final ProductOwner productOwner, final String name, final String description) {
            return new ProductDiscussionRequested(tenant, productId, productOwner, name, description);
        }

        public ProductDiscussionRequested(final Tenant tenant, final ProductId productId, final ProductOwner productOwner, final String name, final String description) {
            this.tenantId = tenant.id;
            this.productId = productId.id;
            this.productOwnerId = productOwner.id;
            this.name = name;
            this.description = description;
            this.ownerId = this.tenantId + ":" + this.productId;
        }
    }
}
