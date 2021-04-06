package io.vlingo.xoom.petclinic.infrastructure;


import io.vlingo.actors.Grid;
import io.vlingo.xoom.XoomInitializationAware;
import io.vlingo.xoom.annotation.initializer.Xoom;

@Xoom(name = "petclinic")
public class Bootstrap implements XoomInitializationAware {

  @Override
  public void onInit(final Grid grid) {
  }

}
