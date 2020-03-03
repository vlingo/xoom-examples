package io.examples.inventory.domain.model;

import io.examples.infra.Identity;
import io.vlingo.xoom.stepflow.StepFlow;
import io.vlingo.xoom.stepflow.StateTransition;

import javax.persistence.Entity;
import java.util.UUID;

@Entity
public class Inventory extends Identity {

    private String productId;
    private InventoryStatus status;

    //@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    //private Reservation reservation;

    //@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    //private Warehouse warehouse;

    public Inventory() {
        this.status = InventoryStatus.INVENTORY_CREATED;
    }

    public InventoryStatus getStatus() {
        return status;
    }

    public void setStatus(InventoryStatus status) {
        this.status = status;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @SuppressWarnings("unchecked")
    public Inventory sendEvent(StepFlow processor, InventoryStatus targetState) {
        InventoryEvent event = new InventoryEvent(status.name(), targetState.name());
        StateTransition stateTransition = processor.applyEvent(event).await();
        stateTransition.apply(this);
        this.status = InventoryStatus.valueOf(stateTransition.getTargetName());
        this.version = UUID.randomUUID().toString();
        return this;
    }
}