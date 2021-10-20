package io.vlingo.xoom.examples.spaces;

import io.vlingo.xoom.lattice.grid.Grid;
import io.vlingo.xoom.turbo.XoomInitializationAware;
import io.vlingo.xoom.turbo.annotation.initializer.ResourceHandlers;
import io.vlingo.xoom.turbo.annotation.initializer.Xoom;

@Xoom(name = "distributed-spaces")
@ResourceHandlers(packages = "io.vlingo.xoom.examples.spaces.resources")
public class Bootstrap implements XoomInitializationAware {

    @Override
    public void onInit(Grid grid) {

    }
}
