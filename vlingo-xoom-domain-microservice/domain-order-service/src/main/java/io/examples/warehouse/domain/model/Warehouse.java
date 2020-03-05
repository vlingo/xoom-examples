package io.examples.warehouse.domain.model;

import io.examples.infra.Identity;
import io.examples.inventory.domain.model.Inventory;
import io.vlingo.xoom.stepflow.StepFlow;
import io.vlingo.xoom.stepflow.StateTransition;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
public class Warehouse extends Identity {

    private WarehouseStatus status;

    @OneToOne(cascade = CascadeType.ALL)
    private WarehouseAddress address;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Inventory> inventory;

    public Warehouse() {
        this.status = WarehouseStatus.WAREHOUSE_CREATED;
    }

    public WarehouseStatus getStatus() {
        return status;
    }

    public void setStatus(WarehouseStatus status) {
        this.status = status;
    }

    public WarehouseAddress getAddress() {
        return address;
    }

    public void setAddress(WarehouseAddress address) {
        this.address = address;
    }

    public List<Inventory> getInventory() {
        return inventory;
    }

    public void setInventory(List<Inventory> inventory) {
        this.inventory = inventory;
    }

    @SuppressWarnings("unchecked")
    public Warehouse sendEvent(StepFlow processor, WarehouseStatus targetState) {
        WarehouseEvent event = new WarehouseEvent(status.name(), targetState.name());
        StateTransition stateTransition = processor.applyEvent(event).await();
        stateTransition.apply(this);
        this.status = WarehouseStatus.valueOf(stateTransition.getTargetName());
        this.version = UUID.randomUUID().toString();
        return this;
    }
}
