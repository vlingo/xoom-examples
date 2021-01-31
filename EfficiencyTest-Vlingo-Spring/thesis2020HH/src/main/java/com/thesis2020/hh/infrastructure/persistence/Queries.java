package com.thesis2020.hh.infrastructure.persistence;


import io.vlingo.common.Completes;
import java.util.Collection;
import com.thesis2020.hh.infrastructure.data.GreetingData;


public interface Queries {
    Completes<GreetingData> greetingWithId(final String id);
    Completes<Collection<GreetingData>> greetings();
}
