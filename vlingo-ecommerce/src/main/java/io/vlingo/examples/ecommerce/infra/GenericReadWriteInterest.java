package io.vlingo.examples.ecommerce.infra;

import io.vlingo.actors.Logger;
import io.vlingo.common.Outcome;
import io.vlingo.lattice.model.projection.ProjectionControl;
import io.vlingo.symbio.Metadata;
import io.vlingo.symbio.Source;
import io.vlingo.symbio.store.Result;
import io.vlingo.symbio.store.StorageException;
import io.vlingo.symbio.store.state.StateStore;

import java.util.List;
import java.util.function.BiConsumer;

class GenericReadWriteInterest implements StateStore.ReadResultInterest, StateStore.WriteResultInterest {

  private final Logger logger;

  GenericReadWriteInterest(Logger logger) {
    this.logger = logger;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <S> void readResultedIn(final Outcome<StorageException, Result> outcome, final String id, final S state, final int stateVersion, final Metadata metadata, final Object object) {
    outcome.andThen(result -> {
      ((BiConsumer<S, Integer>) object).accept(state, stateVersion);
      return result;
    }).otherwise(cause -> {
      if (cause.result.isNotFound()) {
        ((BiConsumer<S, Integer>) object).accept(null, -1);
      }
      logger.info("Query state not read for update because: " + cause.getMessage(), cause);
      return cause.result;
    });
  }

  @Override
  public <S, C> void writeResultedIn(final Outcome<StorageException, Result> outcome, final String id, final S state, final int stateVersion, final List<Source<C>> sources, final Object object) {
    outcome.andThen(result -> {
      ((ProjectionControl.Confirmer) object).confirm();
      return result;
    }).otherwise(cause -> {
      // log but don't retry, allowing re-delivery of Projectable
      logger.info("Query state not written for update because: " + cause.getMessage(), cause);
      return cause.result;
    });
  }
}
