package com.anradev.licenseservice.events.handler;

import com.anradev.licenseservice.events.CustomChannels;
import com.anradev.licenseservice.events.model.OrganizationChangeModel;
import com.anradev.licenseservice.model.Organization;
import com.anradev.licenseservice.repository.DataCacheRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

@EnableBinding(CustomChannels.class)
public class OrganizationChangeHandler {

    private static final Logger logger = LoggerFactory.getLogger(OrganizationChangeHandler.class);

    @Autowired
    @Qualifier("OrganizationRedisRepository")
    private DataCacheRepository<Organization> organizationRedisRepository;

    @StreamListener("inboundOrgChanges")
    public void loggerSink(OrganizationChangeModel organization) {

        logger.debug("Received a message of type " + organization.getType());

        switch(organization.getAction()){
            case "GET":
                logger.debug("Received a GET event from the organization service for organization id {}",
                        organization.getOrganizationId());
                break;
            case "SAVE":
                logger.debug("Received a SAVE event from the organization service for organization id {}",
                        organization.getOrganizationId());
                break;
            case "UPDATE":
                logger.debug("Received a UPDATE event from the organization service for organization id {}",
                        organization.getOrganizationId());

                invalidateOrganizationFromRedisCache(organization.getOrganizationId());
                break;
            case "DELETE":
                logger.debug("Received a DELETE event from the organization service for organization id {}",
                        organization.getOrganizationId());

                invalidateOrganizationFromRedisCache(organization.getOrganizationId());
                break;
            default:
                logger.error("Received an UNKNOWN event from the organization service of type {}",
                        organization.getType());
                break;
        }
    }

    private void invalidateOrganizationFromRedisCache(String orgId) {
        if (organizationRedisRepository.delete("Organization", orgId)) {
            logger.debug("Invalidated cache for organization id {}", orgId);
        }
        else {
            logger.debug("Cannot invalidate cache for organization id {}", orgId);
        }
    }
}
