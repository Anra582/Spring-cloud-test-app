package com.anradev.licenseservice.service.client;

import com.anradev.licenseservice.model.Organization;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @author Aleksei Zhvakin
 */
@Component
public class OrganizationRestClient {

    @Autowired
    private KeycloakRestTemplate restTemplate;

    public Organization getOrganization(String organizationId) {
        ResponseEntity<Organization> response = restTemplate
                .exchange("http://gateway:8072/organization/v1/organization/{organizationId}",
                        HttpMethod.GET, null, Organization.class, organizationId);
        return response.getBody();
    }
}
