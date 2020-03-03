// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.examples.ecommerce.infra;

import io.vlingo.actors.Actor;
import io.vlingo.examples.ecommerce.model.CartUserSummaryData;
import io.vlingo.lattice.model.projection.Projectable;
import io.vlingo.lattice.model.projection.Projection;
import io.vlingo.lattice.model.projection.ProjectionControl;
import io.vlingo.symbio.BaseEntry;
import io.vlingo.symbio.Entry;
import io.vlingo.symbio.store.state.StateStore;

import java.util.Optional;
import java.util.function.BiConsumer;

import static io.vlingo.examples.ecommerce.model.CartEvents.*;


public class CartSummaryProjectionActor extends Actor implements Projection {

  private static final int INITIAL_STATE_VERSION = 1;
  private final StateStore store;
  private EventAdapter<CreatedForUser> createdEventAdapter;
  private EventAdapter<AllItemsRemovedEvent> allItemsRemovedAdapter;
  private EventAdapter<ProductQuantityChangeEvent> productQuantityChangedAdapter;
  private GenericReadWriteInterest genericReadWriteInterest;

  public CartSummaryProjectionActor() {
    this.store = CartQueryProvider.instance().store;
    this.genericReadWriteInterest = new GenericReadWriteInterest(logger());
    this.createdEventAdapter = new EventAdapter<>(CreatedForUser.class);
    this.allItemsRemovedAdapter = new EventAdapter<>(AllItemsRemovedEvent.class);
    this.productQuantityChangedAdapter = new EventAdapter<>(ProductQuantityChangeEvent.class);
  }

  protected boolean matchesTypeName(Entry<?> entry, Class<?> clazz) {
    return entry.type().equals(clazz.getTypeName());
  }

  protected CartUserSummaryData createDataFromEvent(Entry<?> entry) {

    if (matchesTypeName(entry, CreatedForUser.class)) {
      CreatedForUser event = createdEventAdapter.fromEntry((BaseEntry.TextEntry) entry);
      return new CartUserSummaryData(event.userId.getId(), event.cartId, "0");

    } else if (matchesTypeName(entry, AllItemsRemovedEvent.class)) {
      AllItemsRemovedEvent event = allItemsRemovedAdapter.fromEntry((BaseEntry.TextEntry) entry);
      return new CartUserSummaryData(event.userId.getId(), event.cartId, "0");

    } else if (matchesTypeName(entry, ProductQuantityChangeEvent.class)) {
      final ProductQuantityChangeEvent event = productQuantityChangedAdapter.fromEntry((BaseEntry.TextEntry) entry);
      return CartUserSummaryData.from(event.userId.getId(),
                                      event.cartId,
                                      Integer.toString(event.newQuantity));
    } else {
      throw new IllegalArgumentException("Unknown event type: " + entry.type());
    }

  }

  @Override
  public void projectWith(final Projectable projectable, final ProjectionControl control) {
    Optional<Entry<?>> optionalEntry = projectable.entries().stream().findFirst();
    if (!optionalEntry.isPresent()) {
      control.confirmProjected(projectable.projectionId());
      return;
    }
    final Entry<?> entry = optionalEntry.get();
    final CartUserSummaryData nextData = createDataFromEvent(entry);

    BiConsumer<CartUserSummaryData, Integer> updater = (prevObject, prevVersion) -> {
      int version = (prevVersion < 0) ? INITIAL_STATE_VERSION : prevVersion+1;
      store.write(nextData.userId, nextData, version, genericReadWriteInterest, ProjectionControl.confirmerFor(projectable, control));
    };

    store.read(nextData.userId, CartUserSummaryData.class, genericReadWriteInterest, updater);
  }
}
