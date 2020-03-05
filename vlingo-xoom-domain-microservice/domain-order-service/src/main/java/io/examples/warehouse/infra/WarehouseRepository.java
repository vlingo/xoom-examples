package io.examples.warehouse.infra;

import io.examples.warehouse.domain.model.Warehouse;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface WarehouseRepository extends CrudRepository<Warehouse, Long> {
}
