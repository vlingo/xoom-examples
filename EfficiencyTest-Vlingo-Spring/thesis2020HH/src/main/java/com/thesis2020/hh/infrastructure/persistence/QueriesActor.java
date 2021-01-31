package com.thesis2020.hh.infrastructure.persistence;


import io.vlingo.common.Completes;
import io.vlingo.lattice.query.StateStoreQueryActor;
import io.vlingo.symbio.store.state.StateStore;
import java.util.ArrayList;
import java.util.Collection;
import com.thesis2020.hh.infrastructure.data.GreetingData;


public class QueriesActor extends StateStoreQueryActor implements Queries {

    public QueriesActor(final StateStore stateStore){
        super(stateStore);
    }

    @Override
    public Completes<GreetingData> greetingWithId(String id) {
        return queryStateFor(id,GreetingData.class, GreetingData.Empty);
    }

    @Override
    public Completes<Collection<GreetingData>> greetings() {
        return streamAllOf(GreetingData.class,new ArrayList<>());
    }
}
