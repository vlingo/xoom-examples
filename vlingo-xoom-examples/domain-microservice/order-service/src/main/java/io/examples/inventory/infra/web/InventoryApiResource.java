package io.examples.inventory.infra.web;

import io.examples.inventory.application.InventoryService;
import io.examples.inventory.domain.model.Inventory;
import io.vlingo.common.Completes;
import io.vlingo.http.Response;
import io.vlingo.http.resource.ObjectResponse;
import io.vlingo.http.resource.RequestHandler;
import io.vlingo.xoom.resource.annotations.Resource;
import io.vlingo.xoom.resource.Endpoint;

import javax.inject.Provider;

import static io.vlingo.http.Response.Status.Created;
import static io.vlingo.http.Response.Status.Ok;
import static io.vlingo.http.resource.ResourceBuilder.get;
import static io.vlingo.http.resource.ResourceBuilder.post;

@Resource
public class InventoryApiResource implements Endpoint {

    private static final String ENDPOINT_VERSION = "1.0.0";
    private static final String ENDPOINT_NAME = "Inventory";
    private final Provider<InventoryService> inventoryProvider;

    public InventoryApiResource(Provider<InventoryService> inventoryProvider) {
        this.inventoryProvider = inventoryProvider;
    }

    @Override
    public RequestHandler[] getHandlers() {
        return new RequestHandler[]{
                post("/v1/inventory")
                        .body(Inventory.class)
                        .handle(this::defineInventory)
                        .onError(this::getErrorResponse),
                get("/v1/inventory/{id}")
                        .param(Long.class)
                        .handle(this::queryInventory)
                        .onError(this::getErrorResponse)
        };
    }

    private Completes<ObjectResponse<Inventory>> defineInventory(Inventory inventory) {
        return responseWithBody(Created, inventoryProvider.get().defineInventory(inventory));
    }

    private Completes<Response> queryInventory(Long id) {
        return response(Ok, inventoryProvider.get().queryInventory(id));
    }

    @Override
    public String getName() {
        return ENDPOINT_NAME;
    }

    @Override
    public String getVersion() {
        return InventoryApiResource.ENDPOINT_VERSION;
    }
}
