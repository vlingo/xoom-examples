// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.splitter;

/**
 * OrderProcessor defines the API for placing an {@link Order}.
 */
public interface OrderProcessor
{
    // CONSTANTS
    public static final String ITEM_TYPE_A = "TypeA";
    public static final String ITEM_TYPE_B = "TypeB";
    public static final String ITEM_TYPE_C = "TypeC";
    
    // API
    void placeOrder( Order order );
}
