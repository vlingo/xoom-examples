// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.pointtopointchannel;

/**
 * PointToPointProcessor provides a simple method to verify that peer-to-peer messages are received in order.
 */
public interface PointToPointProcessor
{
    void process( Integer messageId );
}
