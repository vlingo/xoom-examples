package io.examples.inventory.infra.repository;

import io.examples.inventory.domain.model.Inventory;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface InventoryRepository extends CrudRepository<Inventory, Long> {
}
