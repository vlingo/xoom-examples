package io.vlingo.xoom.examples.petclinic.infrastructure.exchange;

import io.vlingo.xoom.lattice.grid.Grid;
import io.vlingo.xoom.turbo.actors.Settings;
import io.vlingo.xoom.lattice.exchange.Exchange;
import io.vlingo.xoom.turbo.exchange.ExchangeSettings;
import io.vlingo.xoom.turbo.exchange.ExchangeInitializer;
import io.vlingo.xoom.lattice.exchange.rabbitmq.ExchangeFactory;
import io.vlingo.xoom.lattice.exchange.ConnectionSettings;
import io.vlingo.xoom.lattice.exchange.rabbitmq.Message;
import io.vlingo.xoom.lattice.exchange.rabbitmq.MessageSender;
import io.vlingo.xoom.lattice.exchange.Covey;
import io.vlingo.xoom.symbio.store.dispatch.Dispatcher;

import io.vlingo.xoom.lattice.model.IdentifiedDomainEvent;

public class ExchangeBootstrap implements ExchangeInitializer {

  private Dispatcher dispatcher;

  @Override
  public void init(final Grid stage) {
    ExchangeSettings.load(Settings.properties());

    final ConnectionSettings petclinicSettings =
                ExchangeSettings.of("petclinic").mapToConnection();

    final Exchange petclinic =
                ExchangeFactory.fanOutInstance(petclinicSettings, "petclinic", true);

    petclinic.register(Covey.of(
        new MessageSender(petclinic.connection()),
        received -> {},
        new VeterinarianProducerAdapter(),
        IdentifiedDomainEvent.class,
        IdentifiedDomainEvent.class,
        Message.class));

    petclinic.register(Covey.of(
        new MessageSender(petclinic.connection()),
        received -> {},
        new AnimalTypeProducerAdapter(),
        IdentifiedDomainEvent.class,
        IdentifiedDomainEvent.class,
        Message.class));

    petclinic.register(Covey.of(
        new MessageSender(petclinic.connection()),
        received -> {},
        new PetProducerAdapter(),
        IdentifiedDomainEvent.class,
        IdentifiedDomainEvent.class,
        Message.class));

    petclinic.register(Covey.of(
        new MessageSender(petclinic.connection()),
        received -> {},
        new ClientProducerAdapter(),
        IdentifiedDomainEvent.class,
        IdentifiedDomainEvent.class,
        Message.class));

    petclinic.register(Covey.of(
        new MessageSender(petclinic.connection()),
        received -> {},
        new SpecialtyTypeProducerAdapter(),
        IdentifiedDomainEvent.class,
        IdentifiedDomainEvent.class,
        Message.class));


    this.dispatcher = new ExchangeDispatcher(petclinic);

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        petclinic.close();

        System.out.println("\n");
        System.out.println("==================");
        System.out.println("Stopping exchange.");
        System.out.println("==================");
    }));
  }

  @Override
  public Dispatcher dispatcher() {
    return dispatcher;
  }
}