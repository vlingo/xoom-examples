package com.thesis2020.hh.infrastructure.persistence;


import io.vlingo.common.Completes;

import java.util.Collection;

import com.thesis2020.hh.model.greeting.GreetingState;

public interface Queries {
    Completes<GreetingState> greetingWithId(final String id);
    Completes<Collection<GreetingState>> greetings();
}
