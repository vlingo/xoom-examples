package io.examples.order.domain;

/**
 * {@code DomainEventNotifier} notifies whoever is interested in
 * a {@link DomainEvent}.
 *
 * @author Danilo Ambrosio
 */
public interface DomainEventNotifier {

    public void notify(final DomainEvent domainEvent);

}
