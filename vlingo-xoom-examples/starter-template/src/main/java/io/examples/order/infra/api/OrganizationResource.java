package io.examples.order.infra.api;

import io.examples.order.domain.Organization;
import io.examples.order.domain.OrganizationService;
import io.vlingo.common.Completes;
import io.vlingo.http.Response;
import io.vlingo.http.resource.RequestHandler;
import io.vlingo.xoom.resource.annotations.Resource;
import io.vlingo.xoom.resource.Endpoint;

import static io.vlingo.http.Response.Status.Created;
import static io.vlingo.http.Response.Status.Ok;
import static io.vlingo.http.resource.ResourceBuilder.*;

@Resource
public class OrganizationResource implements Endpoint {

    private static final String ENDPOINT_VERSION = "1.0.0";
    private static final String ENDPOINT_NAME = "Organization";
    private final OrganizationService organizationService;

    public OrganizationResource(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @Override
    public RequestHandler[] getHandlers() {
        return new RequestHandler[]{
                get("/organizations/{id}")
                        .param(Long.class)
                        .handle(this::queryOrganization)
                        .onError(this::getErrorResponse),
                post("/organizations")
                        .body(Organization.class)
                        .handle(this::defineOrganization)
                        .onError(this::getErrorResponse),
                patch("/organizations/{id}/enable")
                        .param(Long.class)
                        .handle(this::enableOrganization)
                        .onError(this::getErrorResponse),
                patch("/organizations/{id}/disable")
                        .param(Long.class)
                        .handle(this::disableOrganization)
                        .onError(this::getErrorResponse)
        };
    }

    public Completes<Response> queryOrganization(Long id) {
        return response(Ok, organizationService.queryOrganization(id));
    }

    public Completes<Response> defineOrganization(Organization organization) {
        return response(Created, organizationService.defineOrganization(organization));
    }

    public Completes<Response> enableOrganization(Long id) {
        return response(Created, organizationService.enableOrganization(id));
    }

    public Completes<Response> disableOrganization(Long id) {
        return response(Created, organizationService.disableOrganization(id));
    }

    @Override
    public String getName() {
        return ENDPOINT_NAME;
    }

    @Override
    public String getVersion() {
        return OrganizationResource.ENDPOINT_VERSION;
    }
}
