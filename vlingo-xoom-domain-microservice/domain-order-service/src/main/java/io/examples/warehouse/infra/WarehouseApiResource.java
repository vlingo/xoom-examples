package io.examples.warehouse.infra;

import io.examples.warehouse.application.WarehouseService;
import io.examples.warehouse.domain.model.Warehouse;
import io.vlingo.common.Completes;
import io.vlingo.http.Response;
import io.vlingo.http.resource.RequestHandler;
import io.vlingo.xoom.resource.annotations.Resource;
import io.vlingo.xoom.resource.Endpoint;

import javax.inject.Provider;

import static io.vlingo.http.Response.Status.Created;
import static io.vlingo.http.Response.Status.Ok;
import static io.vlingo.http.resource.ResourceBuilder.get;
import static io.vlingo.http.resource.ResourceBuilder.post;

@Resource
public class WarehouseApiResource implements Endpoint {

    private static final String ENDPOINT_VERSION = "1.0.0";
    private static final String ENDPOINT_NAME = "Warehouse";
    private final Provider<WarehouseService> warehouseProvider;

    public WarehouseApiResource(Provider<WarehouseService> warehouseProvider) {
        this.warehouseProvider = warehouseProvider;
    }

    @Override
    public RequestHandler[] getHandlers() {
        return new RequestHandler[]{
                post("/v1/warehouses")
                        .body(Warehouse.class)
                        .handle(this::defineWarehouse)
                        .onError(this::getErrorResponse),
                get("/v1/warehouses/{id}")
                        .param(Long.class)
                        .handle(this::queryWarehouse)
                        .onError(this::getErrorResponse)
        };
    }

    private Completes<Response> defineWarehouse(Warehouse warehouse) {
        return response(Created, warehouseProvider.get().defineWarehouse(warehouse));
    }

    private Completes<Response> queryWarehouse(Long id) {
        return response(Ok, warehouseProvider.get().queryWarehouse(id));
    }

    @Override
    public String getName() {
        return ENDPOINT_NAME;
    }

    @Override
    public String getVersion() {
        return WarehouseApiResource.ENDPOINT_VERSION;
    }
}
