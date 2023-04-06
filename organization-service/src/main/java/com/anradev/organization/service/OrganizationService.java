package com.anradev.organization.service;

import com.anradev.organization.events.source.OrganizationMessagePublisher;
import com.anradev.organization.model.Organization;
import com.anradev.organization.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository repository;

    @Autowired
    private OrganizationMessagePublisher messagePublisher;

    public Organization findById(String organizationId) {
        Optional<Organization> opt = repository.findById(organizationId);
        messagePublisher.publishOrganizationChange("GET", organizationId);
        return opt.orElse(null);
    }

    public Organization create(Organization organization){
        organization.setId(UUID.randomUUID().toString());
        organization = repository.save(organization);
        messagePublisher.publishOrganizationChange("SAVE", organization.getId());
        return organization;

    }

    public void update(Organization organization){
        repository.save(organization);
        messagePublisher.publishOrganizationChange("UPDATE", organization.getId());
    }

    public void delete(Organization organization){
        repository.deleteById(organization.getId());
        messagePublisher.publishOrganizationChange("DELETE", organization.getId());
    }
}
