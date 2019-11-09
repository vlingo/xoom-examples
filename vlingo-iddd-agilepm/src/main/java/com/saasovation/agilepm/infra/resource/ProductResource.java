package com.saasovation.agilepm.infra.resource;

import com.saasovation.agilepm.model.Tenant;
import com.saasovation.agilepm.model.product.Product;
import com.saasovation.agilepm.model.product.ProductEntity;
import com.saasovation.agilepm.model.product.ProductId;
import com.saasovation.agilepm.model.product.ProductOwner;
import io.vlingo.actors.*;
import io.vlingo.common.Completes;
import io.vlingo.common.identity.IdentityGeneratorType;
import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.http.Response;
import io.vlingo.http.resource.Resource;
import io.vlingo.lattice.grid.GridAddressFactory;

import static io.vlingo.http.Response.Status.Created;
import static io.vlingo.http.Response.Status.InternalServerError;
import static io.vlingo.http.resource.ResourceBuilder.post;
import static io.vlingo.http.resource.ResourceBuilder.resource;

public class ProductResource {

    private static final String ROOT_URL = "/product";
    private final Stage stage;
    private final AddressFactory addressFactory;

    public ProductResource(final World world) {
        this.stage = world.stage();
        this.addressFactory = new GridAddressFactory(IdentityGeneratorType.RANDOM);
    }

    public Resource<?> routes() {
        return resource("Product resource fluent api",
                post(ROOT_URL)
                        .body(ProductCreateRequest.class)
                        .handle(this::create));
    }

    private Completes<Response> create(final ProductCreateRequest request) {
        try {
            final ProductId productId = ProductId.unique();
            final Address productAddress = addressFactory.from(productId.id);
            final Tenant tenant = Tenant.fromExisting(request.tenantId);
            final Definition definition = Definition.has(ProductEntity.class, Definition.parameters(tenant, productId));

            final Product product = stage.actorFor(Product.class, definition, productAddress);
            product.define(ProductOwner.with(tenant, request.ownerId), request.name, request.description, request.hasDiscussion);

            return Completes.withSuccess(Response.of(Created, JsonSerialization.serialized(productId)));
        } catch (Throwable t) {
            this.stage.world().defaultLogger().error("Failed to create the product", t);
            return Completes.withSuccess(Response.of(InternalServerError));
        }
    }


    public static class ProductCreateRequest {
        final String tenantId;
        final String ownerId;
        final String name;
        final String description;
        final boolean hasDiscussion;

        ProductCreateRequest(final String tenantId, final String ownerId, final String name, final String description, final boolean hasDiscussion) {
            this.tenantId = tenantId;
            this.ownerId = ownerId;
            this.name = name;
            this.description = description;
            this.hasDiscussion = hasDiscussion;
        }
    }
}
