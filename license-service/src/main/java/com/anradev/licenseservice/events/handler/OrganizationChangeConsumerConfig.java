package com.anradev.licenseservice.events.handler;

import com.anradev.licenseservice.events.model.OrganizationChangeModel;
import com.anradev.licenseservice.model.Organization;
import com.anradev.licenseservice.repository.DataCacheRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

/**
 * @author Aleksei Zhvakin
 */
@Slf4j
@Configuration
public class OrganizationChangeConsumerConfig {

    @Autowired
    @Qualifier("OrganizationRedisRepository")
    private DataCacheRepository<Organization> organizationRedisRepository;

    @Bean
    Consumer<OrganizationChangeModel> OrganizationChangeConsumer() {
        return organization -> {
            switch (organization.getAction()) {
                case "GET" -> log.debug("Received a GET event from the organization service for organization id {}",
                        organization.getOrganizationId());
                case "SAVE" -> log.debug("Received a SAVE event from the organization service for organization id {}",
                        organization.getOrganizationId());
                case "UPDATE" -> {
                    log.debug("Received a UPDATE event from the organization service for organization id {}",
                            organization.getOrganizationId());
                    invalidateOrganizationFromRedisCache(organization.getOrganizationId());
                }
                case "DELETE" -> {
                    log.debug("Received a DELETE event from the organization service for organization id {}",
                            organization.getOrganizationId());
                    invalidateOrganizationFromRedisCache(organization.getOrganizationId());
                }
                default -> log.error("Received an UNKNOWN event from the organization service of type {}",
                        organization.getType());
            }
        };
    }

    private void invalidateOrganizationFromRedisCache(String orgId) {
        if (organizationRedisRepository.delete("Organization", orgId)) {
            log.debug("Invalidated cache for organization id {}", orgId);
        }
        else {
            log.debug("Cannot invalidate cache for organization id {}", orgId);
        }
    }
}
