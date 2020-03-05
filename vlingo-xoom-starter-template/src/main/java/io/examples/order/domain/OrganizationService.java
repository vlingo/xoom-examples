package io.examples.order.domain;

import io.examples.order.infra.stepflow.StepFlowService;
import io.examples.order.infra.repository.OrganizationRepository;
import io.vlingo.actors.Logger;
import io.vlingo.common.Completes;

import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import java.util.function.Consumer;

@Singleton
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final StepFlowService stepFlowService;

    public OrganizationService(OrganizationRepository organizationRepository, StepFlowService stepFlowService) {
        this.organizationRepository = organizationRepository;
        this.stepFlowService = stepFlowService;
    }

    public Completes<Organization> queryOrganization(Long id) {
        return organizationRepository.findById(id).map(Completes::withSuccess).orElseThrow(() ->
                new RuntimeException("Organization with id[" + id + "] does not exist"));
    }

    public Completes<Organization> defineOrganization(Organization model) {
        // Execute the create command on a new organization entity
        return executeCommand(organizationRepository.save(model).getId(),
                organization ->
                        organization.define(stepFlowService.getFlow()))
                .andThenConsume(organization ->
                        queryOrganization(organization.getId()))
                .otherwise(organization -> {
                    throw new RuntimeException("Could not define the organization: " + model.toString());
                });
    }

    public Completes<Organization> enableOrganization(Long organizationId) {
        // Execute the confirm command on the organization
        return executeCommand(organizationId,
                organization -> organization.enable(stepFlowService.getFlow()))
                .otherwise(organization -> {
                    throw new RuntimeException("Could not enable the organization: " + organization.toString());
                });
    }

    public Completes<Organization> disableOrganization(Long organizationId) {
        // Execute the confirm command on the organization
        return executeCommand(organizationId,
                organization -> organization.disable(stepFlowService.getFlow()))
                .otherwise(organization -> {
                    throw new RuntimeException("Could not disable the organization: " + organization.toString());
                });
    }

    private Completes<Organization> executeCommand(@NotNull Long id, Consumer<Organization> commandHandler) {
        return queryOrganization(id)
                .andThenConsume(commandHandler)
                .andThen(organization -> {
                    // The command succeeded, now persist the updated state to the repository
                    Logger.basicLogger().info("Updated organization: " + organization.toString());
                    organizationRepository.update(id, organization.getStatus(), organization.getVersion());
                    return organization;
                });
    }
}
