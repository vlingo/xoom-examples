// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.examples.ecommerce.model;

import io.vlingo.common.Completes;
import io.vlingo.lattice.query.StateStoreQueryActor;
import io.vlingo.symbio.store.state.StateStore;

public class CartQueryActor extends StateStoreQueryActor implements CartQuery {
    public CartQueryActor(final StateStore store) {
        super(store);
    }

    @Override
    public Completes<CartUserSummaryData> getCartSummaryForUser(UserId userId) {
        return queryStateFor(userId.getId(), CartUserSummaryData.class);
    }
}
