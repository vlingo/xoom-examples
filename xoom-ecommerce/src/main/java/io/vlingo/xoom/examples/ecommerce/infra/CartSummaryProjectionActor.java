// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.ecommerce.infra;

import java.util.ArrayList;
import java.util.List;

import io.vlingo.xoom.examples.ecommerce.model.CartEvents.AllItemsRemovedEvent;
import io.vlingo.xoom.examples.ecommerce.model.CartEvents.CreatedForUser;
import io.vlingo.xoom.examples.ecommerce.model.CartEvents.ProductQuantityChangeEvent;
import io.vlingo.xoom.examples.ecommerce.model.CartUserSummaryData;
import io.vlingo.xoom.lattice.model.DomainEvent;
import io.vlingo.xoom.lattice.model.IdentifiedDomainEvent;
import io.vlingo.xoom.lattice.model.projection.Projectable;
import io.vlingo.xoom.lattice.model.projection.StateStoreProjectionActor;
import io.vlingo.xoom.symbio.Entry;


public class CartSummaryProjectionActor extends StateStoreProjectionActor<CartUserSummaryData> {
  private static final CartUserSummaryData Empty = CartUserSummaryData.empty();
  
  private String dataId;
  private final List<IdentifiedDomainEvent> events;

  public CartSummaryProjectionActor() {
    super(CartQueryProvider.instance().store);
    
    this.events = new ArrayList<>(2);
  }

  @Override
  protected CartUserSummaryData currentDataFor(final Projectable projectable) {
	return Empty;
  }

  @Override
  protected String dataIdFor(final Projectable projectable) {
    dataId = events.get(0).identity();
    return dataId;
  }

  @Override
  protected CartUserSummaryData merge(
		  final CartUserSummaryData previousData,
		  final int previousVersion,
		  final CartUserSummaryData currentData,
		  final int currentVersion) {

    if (previousVersion == currentVersion) return currentData;

    CartUserSummaryData merged = previousData;
    for (final DomainEvent event : events) {
      switch (match(event)) {
      case CreatedForUser:
        final CreatedForUser created = typed(event);
        merged = CartUserSummaryData.from(created.userId.getId(), created.cartId, 0);
        break;

      case AllItemsRemovedEvent:
        final AllItemsRemovedEvent removed = typed(event);
        merged = CartUserSummaryData.from(removed.userId.getId(), removed.cartId, 0);
        break;

      case ProductQuantityChangeEvent:
        final ProductQuantityChangeEvent changed = typed(event);
        merged = merged.mergeWith(changed.userId.getId(), changed.cartId, 1);
        break;

      case Unmatched:
        logger().warn("Event of type " + event.typeName() + " was not matched.");
        break;
      }
    }

    return merged;
  }

  @Override
  protected void prepareForMergeWith(final Projectable projectable) {
    events.clear();

    for (Entry <?> entry : projectable.entries()) {
      events.add(entryAdapter().anyTypeFromEntry(entry));
    }
  }

  private CartUserSummaryDataProjectableType match(final DomainEvent event) {
    try {
      return CartUserSummaryDataProjectableType.valueOf(event.typeName());
    } catch (Exception e) {
      return CartUserSummaryDataProjectableType.Unmatched;
    }
  }
}
