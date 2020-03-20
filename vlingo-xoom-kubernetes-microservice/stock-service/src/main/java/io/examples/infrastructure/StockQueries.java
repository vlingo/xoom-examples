package io.examples.infrastructure;

import io.examples.stock.domain.Location;
import io.examples.stock.domain.StockState;
import io.vlingo.common.Completes;

public interface StockQueries {

    Completes<StockState> queryByLocation(final Location location);

}
