// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.examples.reactive.messaging.patterns.splitter;

import io.vlingo.xoom.examples.reactive.messaging.patterns.splitter.Order.OrderItem;

/**
 * OrderItemProcessor defines the API for processing an individual {@link OrderItem}.
 */
public interface OrderItemProcessor
{
    void orderTypeAItem( OrderItem orderItem );
    void orderTypeBItem( OrderItem orderItem );
    void orderTypeCItem( OrderItem orderItem );
}
