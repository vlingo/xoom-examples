package com.saasovation.collaboration.infra.exchange.product.model;

import io.vlingo.lattice.model.DomainEvent;

public class ProductDiscussionRequested extends DomainEvent {
  public final String productId;
  public final String ownerId;
  public final String tenantId;
  public final String productOwnerId;
  public final String description;
  public final String name;

  public ProductDiscussionRequested(final String productId, final String ownerId,
                                    final String tenantId, final String productOwnerId, final String description,
                                    final String name) {
    this.productId = productId;
    this.ownerId = ownerId;
    this.tenantId = tenantId;
    this.productOwnerId = productOwnerId;
    this.description = description;
    this.name = name;
  }
}
