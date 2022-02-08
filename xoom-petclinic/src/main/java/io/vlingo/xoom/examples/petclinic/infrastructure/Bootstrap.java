package io.vlingo.xoom.examples.petclinic.infrastructure;


import io.vlingo.xoom.lattice.grid.Grid;
import io.vlingo.xoom.turbo.ComponentRegistry;
import io.vlingo.xoom.turbo.XoomInitializationAware;
import io.vlingo.xoom.turbo.annotation.initializer.Xoom;

@Xoom(name = "petclinic")
public class Bootstrap implements XoomInitializationAware {

  @Override
  public void onInit(final Grid grid) {
    ComponentRegistry.register(Grid.class, grid);
  }

}
