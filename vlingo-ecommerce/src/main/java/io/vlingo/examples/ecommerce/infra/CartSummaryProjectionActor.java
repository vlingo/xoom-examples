// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.examples.ecommerce.infra;

import io.vlingo.actors.Actor;
import io.vlingo.common.Outcome;
import io.vlingo.examples.ecommerce.model.CartEvents;
import io.vlingo.examples.ecommerce.model.CartUserSummaryData;
import io.vlingo.lattice.model.projection.Projectable;
import io.vlingo.lattice.model.projection.Projection;
import io.vlingo.lattice.model.projection.ProjectionControl;
import io.vlingo.lattice.model.projection.ProjectionControl.Confirmer;
import io.vlingo.symbio.BaseEntry;
import io.vlingo.symbio.Entry;
import io.vlingo.symbio.Metadata;
import io.vlingo.symbio.Source;
import io.vlingo.symbio.store.Result;
import io.vlingo.symbio.store.StorageException;
import io.vlingo.symbio.store.state.StateStore;
import io.vlingo.symbio.store.state.StateStore.ReadResultInterest;
import io.vlingo.symbio.store.state.StateStore.WriteResultInterest;

import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;


public class CartSummaryProjectionActor extends Actor
    implements Projection, ReadResultInterest, WriteResultInterest {

  public static final int INITIAL_STATE_VERSION = 1;
  private final ReadResultInterest readInterest;
  private final WriteResultInterest writeInterest;
  private final StateStore store;
  private EventAdapter<CartEvents.CreatedForUser> createdEventAdapter;
  private EventAdapter<CartEvents.AllItemsRemovedEvent> allItemsRemovedAdapter;
  private EventAdapter<CartEvents.ProductQuantityChangeEvent> productQuantityChangedAdapter;

  public CartSummaryProjectionActor() {
    this.store = CartQueryProvider.instance().store;
    this.readInterest = selfAs(ReadResultInterest.class);
    this.writeInterest = selfAs(WriteResultInterest.class);

    this.createdEventAdapter = new EventAdapter<>(CartEvents.CreatedForUser.class);
    this.allItemsRemovedAdapter = new EventAdapter<>(CartEvents.AllItemsRemovedEvent.class);
    this.productQuantityChangedAdapter = new EventAdapter<>(CartEvents.ProductQuantityChangeEvent.class);
  }

  @Override
  public void projectWith(final Projectable projectable, final ProjectionControl control) {
    Collection<Entry<?>> entries = projectable.entries();
    for (Entry<?> entry : entries) {

      if (entry.type().equals(CartEvents.CreatedForUser.class.getTypeName())) {
        CartEvents.CreatedForUser event = createdEventAdapter.fromEntry((BaseEntry.TextEntry) entry);
        CartUserSummaryData summaryData = new CartUserSummaryData(event.userId.getId(),
                                                                  event.cartId,
                                                                  "0");
        Confirmer confirmer = control.confirmerFor(projectable);
        store.write(summaryData.userId, summaryData, INITIAL_STATE_VERSION, writeInterest, confirmer);
      }

      else if (entry.type().equals(CartEvents.AllItemsRemovedEvent.class.getTypeName())) {
        CartEvents.AllItemsRemovedEvent event = allItemsRemovedAdapter.fromEntry((BaseEntry.TextEntry) entry);
        final BiConsumer<CartUserSummaryData, Integer> updater = (previousValue, previousVersion) -> {
          updateWith(previousValue,
                     (previous) -> CartUserSummaryData.from(previous.userId, previous.cartId, "0"),
                     previousVersion+1,
                     control.confirmerFor(projectable));
        };
        store.read(event.userId.getId(), CartUserSummaryData.class, readInterest, updater);
      }

      else if (entry.type().equals(CartEvents.ProductQuantityChangeEvent.class.getTypeName())) {
        final CartEvents.ProductQuantityChangeEvent event = productQuantityChangedAdapter.fromEntry((BaseEntry.TextEntry) entry);

        final Function<CartUserSummaryData, CartUserSummaryData> updateFunction = previous -> {
          int cartItems = Integer.parseInt(previous.numberOfItems);
          return  CartUserSummaryData.from(previous.userId,
                                           previous.cartId,
                                           Integer.toString(cartItems - event.quantityChange));
        };

        final BiConsumer<CartUserSummaryData, Integer> updater = (previous, previousVersion) -> {
          updateWith(previous,
                     updateFunction,
                     previousVersion+1,
                     control.confirmerFor(projectable));
        };
        store.read(event.userId.getId(), CartUserSummaryData.class, readInterest, updater);
      }

    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public <S> void readResultedIn(final Outcome<StorageException, Result> outcome, final String id, final S state, final int stateVersion, final Metadata metadata, final Object object) {
    outcome.andThen(result -> {
      ((BiConsumer<S, Integer>) object).accept(state, stateVersion);
      return result;
    }).otherwise(cause -> {
      // log but don't retry, allowing re-delivery of Projectable
      logger().info("Query state not read for update because: " + cause.getMessage(), cause);
      return cause.result;
    });
  }

  @Override
  public <S,C> void writeResultedIn(final Outcome<StorageException, Result> outcome, final String id, final S state, final int stateVersion, final List<Source<C>> sources, final Object object) {
    outcome.andThen(result -> {
      ((Confirmer) object).confirm();
      return result;
    }).otherwise(cause -> {
      // log but don't retry, allowing re-delivery of Projectable
      logger().info("Query state not written for update because: " + cause.getMessage(), cause);
      return cause.result;
    });
  }


  private void updateWith(final CartUserSummaryData previous,
                          final Function<CartUserSummaryData,CartUserSummaryData> updater,
                          final int version,
                          final Confirmer confirmer) {
    final CartUserSummaryData data = updater.apply(previous);
    store.write(data.userId, data, version, writeInterest, confirmer);
  }

}
