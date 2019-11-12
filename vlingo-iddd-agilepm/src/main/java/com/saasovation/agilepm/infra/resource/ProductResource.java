package com.saasovation.agilepm.infra.resource;

import static io.vlingo.http.Response.Status.Created;
import static io.vlingo.http.Response.Status.InternalServerError;
import static io.vlingo.http.Response.Status.NotFound;
import static io.vlingo.http.Response.Status.Ok;
import static io.vlingo.http.resource.ResourceBuilder.get;
import static io.vlingo.http.resource.ResourceBuilder.patch;
import static io.vlingo.http.resource.ResourceBuilder.post;
import static io.vlingo.http.resource.ResourceBuilder.resource;

import com.saasovation.agilepm.model.Tenant;
import com.saasovation.agilepm.model.product.Product;
import com.saasovation.agilepm.model.product.ProductId;
import com.saasovation.agilepm.model.product.ProductOwner;

import io.vlingo.actors.AddressFactory;
import io.vlingo.actors.Stage;
import io.vlingo.actors.World;
import io.vlingo.common.Completes;
import io.vlingo.common.Tuple2;
import io.vlingo.common.identity.IdentityGeneratorType;
import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.http.Response;
import io.vlingo.http.resource.Resource;
import io.vlingo.lattice.grid.GridAddressFactory;

public class ProductResource {

    private static final String PRODUCTS_URL = "/products";
    private static final String PRODUCT_URL = "/tenants/{tenantId}/products/{productId}";
    private static final String PRODUCT_QUERY_URL = "/products/{productId}";
    
    private final Stage stage;
    private final AddressFactory addressFactory;

    public ProductResource(final World world) {
        this.stage = world.stage();
        this.addressFactory = new GridAddressFactory(IdentityGeneratorType.RANDOM);
    }

    public Resource<?> routes() {
        return resource("Product resource fluent api",
                post(PRODUCTS_URL)
                        .body(ProductDefinition.class)
                        .handle(this::defineWith),
                patch(PRODUCT_URL)
                        .param(String.class)
                        .param(String.class)
                        .body(String.class)
                        .handle(this::changeDescription),
                get(PRODUCT_QUERY_URL)
                        .param(String.class)
                        .handle(this::query));
    }

    private Completes<Response> defineWith(final ProductDefinition productDefinition) {
        try {
          final Tuple2<ProductId, Product> product =
                  Product.defineWith(
                    stage,
                    Tenant.fromExisting(productDefinition.tenantId),
                    ProductOwner.fromExisting(productDefinition.tenantId, productDefinition.ownerId),
                    productDefinition.name,
                    productDefinition.description,
                    productDefinition.hasDiscussion);
          
            return Completes.withSuccess(Response.of(Created, JsonSerialization.serialized(product._1)));
        } catch (Throwable t) {
            this.stage.world().defaultLogger().error("Failed to create the product", t);
            return Completes.withSuccess(Response.of(InternalServerError));
        }
    }

    private Completes<Response> changeDescription(String tenantId, String productId, String description) {
      return stage.actorOf(Product.class, addressFactory.from(productId))
              .andThenTo(product -> product.changeDescription(description))
              .andThenTo(product -> Completes.withSuccess(Response.of(Ok))
              .otherwise(noProduct -> Response.of(NotFound, productId)));
    }

    private Completes<Response> query(String productId) {
      stage.world().defaultLogger().debug("GET Product: " + productId);
      return stage.actorOf(Product.class, addressFactory.from(productId))
              .andThenTo(product -> product.query())
              .andThenTo(state -> Completes.withSuccess(Response.of(Ok, JsonSerialization.serialized(state)))
              .otherwise(noProduct -> Response.of(NotFound, productId)));
    }
    
    public static class ProductDefinition {
        final String tenantId;
        final String ownerId;
        final String name;
        final String description;
        final boolean hasDiscussion;

        ProductDefinition(final String tenantId, final String ownerId, final String name, final String description, final boolean hasDiscussion) {
            this.tenantId = tenantId;
            this.ownerId = ownerId;
            this.name = name;
            this.description = description;
            this.hasDiscussion = hasDiscussion;
        }
    }
}
