// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.examples.reactive.messaging.patterns.messageexpiration;

/**
 * ExpiringMessage defines the behavior required of a message that can expire.
 */
public interface ExpiringMessage
{
    Boolean isExpired();
}
