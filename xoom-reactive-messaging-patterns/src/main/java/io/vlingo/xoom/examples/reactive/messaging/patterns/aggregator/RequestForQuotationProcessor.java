// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.reactive.messaging.patterns.aggregator;

import java.util.Collections;
import java.util.List;

public interface RequestForQuotationProcessor {
  void requestPriceQuotationFor(final RequestForQuotation rfq);

  public final class RequestForQuotation {
    public final List<RetailItem> retailItems;
    public final String rfqId;
    public final Double totalRetailPrice;
    
    public RequestForQuotation(final String rfqId, final List<RetailItem> retailItems) {
      this.rfqId = rfqId;
      this.retailItems = Collections.unmodifiableList(retailItems);
      this.totalRetailPrice = this.retailItems.stream().mapToDouble(retailItem -> retailItem.retailPrice).sum();
    }
  }

  public final class RetailItem {
    public final String itemId;
    public final Double retailPrice;

    public RetailItem(final String itemId, final Double retailPrice) {
      this.itemId = itemId;
      this.retailPrice = retailPrice;
    }
  }
}
