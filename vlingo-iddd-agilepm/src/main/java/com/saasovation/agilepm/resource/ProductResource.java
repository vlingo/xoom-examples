package com.saasovation.agilepm.resource;

import io.vlingo.actors.AddressFactory;
import io.vlingo.actors.Stage;
import io.vlingo.actors.World;

public class ProductResource {

    private final Stage stage;
    private final AddressFactory addressFactory;

    public ProductResource(final World world) {
        this.stage = world.stage();
        this.addressFactory = world.addressFactory();
    }

}
