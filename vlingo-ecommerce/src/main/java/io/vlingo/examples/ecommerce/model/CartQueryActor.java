package io.vlingo.examples.ecommerce.model;

import io.vlingo.actors.Actor;
import io.vlingo.actors.CompletesEventually;
import io.vlingo.common.Completes;
import io.vlingo.common.Outcome;
import io.vlingo.symbio.Metadata;
import io.vlingo.symbio.store.Result;
import io.vlingo.symbio.store.StorageException;
import io.vlingo.symbio.store.state.StateStore;

import java.util.function.BiConsumer;

public class CartQueryActor extends Actor implements CartQuery, StateStore.ReadResultInterest {

    private final StateStore.ReadResultInterest interest;
    private final StateStore store;

    public CartQueryActor(final StateStore store) {
        this.store = store;
        this.interest = selfAs(StateStore.ReadResultInterest.class);
    }

    @Override
    public Completes<CartUserSummaryData> getCartSummaryForUser(int userId) {
        final CompletesEventually completesEventually = completesEventually();
        final BiConsumer<CartUserSummaryData,Integer> translator = (data, version) -> {
            if (data != null) {
                completesEventually.with(data);
            } else {
                completesEventually.with(CartUserSummaryData.empty());
            }
        };
        store.read(Integer.toString(userId), CartUserSummaryData.class, interest, translator);
        return completes(); // return completes associated to finalization
    }

    @Override
    @SuppressWarnings("unchecked")
    public <S> void readResultedIn(final Outcome<StorageException, Result> outcome, final String id, final S state, final int stateVersion, final Metadata metadata, final Object object) {
        outcome.andThen(result -> {
            ((BiConsumer<S,Integer>) object).accept(state, stateVersion);
            return result;
        }).otherwise(cause -> {
            ((BiConsumer<S,Integer>) object).accept(null, -1);
            return cause.result;
        });
    }
}
