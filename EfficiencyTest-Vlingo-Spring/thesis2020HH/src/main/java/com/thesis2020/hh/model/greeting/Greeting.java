package com.thesis2020.hh.model.greeting;


import com.thesis2020.hh.infrastructure.data.GreetingRequestData;
import com.thesis2020.hh.infrastructure.data.GreetingChangeRequestData;
import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;

public interface Greeting {

    static Completes<GreetingState> defineGreeting(Stage stage, GreetingRequestData data){
//        Address address = stage.world().addressFactory().uniquePrefixedWith("g-");
        Greeting greeting = stage.actorFor(Greeting.class, GreetingEntity.class);
        return greeting.defineGreeting(data);
    }


    Completes<GreetingState> defineGreeting(GreetingRequestData data);

    Completes<GreetingState> changeMessage(GreetingChangeRequestData data);

    Completes<GreetingState> changeDescription(GreetingChangeRequestData data);



}