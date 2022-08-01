package com.anradev.licenseservice.service;

import com.anradev.licenseservice.config.ServiceConfig;
import com.anradev.licenseservice.model.License;
import com.anradev.licenseservice.model.Organization;
import com.anradev.licenseservice.repository.DataCacheRepository;
import com.anradev.licenseservice.repository.LicenseRepository;
import com.anradev.licenseservice.repository.OrganizationRedisRepository;
import com.anradev.licenseservice.service.client.OrganizationFeignClient;
import com.anradev.licenseservice.utils.UserContext;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

@Service
public class LicenseService {

    @Autowired
    MessageSource messages;

    @Autowired
    private LicenseRepository licenseRepository;

    @Autowired
    @Qualifier("OrganizationRedisRepository")
    private DataCacheRepository<Organization> organizationRedisRepository;

    @Autowired
    ServiceConfig config;

    @Autowired
    OrganizationFeignClient organizationFeignClient;

    private static final Logger logger = LoggerFactory.getLogger(LicenseService.class);


    public License getLicense(String licenseId, String organizationId){
        License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
        if (null == license) {
            throw new IllegalArgumentException(String.format(messages.getMessage("license.search.error.message", null, null),licenseId, organizationId));
        }

        Organization organization = getOrganization(organizationId);
        if (null != organization) {
            license.setOrganizationName(organization.getName());
            license.setContactName(organization.getContactName());
            license.setContactEmail(organization.getContactEmail());
            license.setContactPhone(organization.getContactPhone());
        }

        return license.withComment(config.getExampleProperty());
    }

    public Organization getOrganization(String organizationId) {
        logger.debug("In Licensing Service.getOrganization: {}", UserContext.getCorrelationId());

        Organization organization = checkRedisCache(organizationId);

        if (organization != null){
            logger.debug("I have successfully retrieved an organization {} from the redis cache: {}", organizationId, organization);
            return organization;
        }

        logger.debug("Unable to locate organization from the redis cache: {}.", organizationId);

        organization = retrieveOrganizationInfo(organizationId);

        if (organization != null) {
            cacheOrganizationObject(organization);
        }
        return organization;
    }

    private Organization retrieveOrganizationInfo(String organizationId) {
        return organizationFeignClient.getOrganization(organizationId);
    }

    private Organization checkRedisCache(String organizationId) {
        return organizationRedisRepository.find("Organization", organizationId, Organization.class);
    }

    private void cacheOrganizationObject(Organization organization) {
        organizationRedisRepository.add("Organization", organization.getId(), organization);
    }

    public License createLicense(License license){
        license.setLicenseId(UUID.randomUUID().toString());
        licenseRepository.save(license);

        return license.withComment(config.getExampleProperty());
    }

    public License updateLicense(License license){
        licenseRepository.save(license);

        return license.withComment(config.getExampleProperty());
    }

    public String deleteLicense(String licenseId){
        String responseMessage;
        License license = new License();
        license.setLicenseId(licenseId);
        licenseRepository.delete(license);
        responseMessage = String.format(messages.getMessage("license.delete.message", null, null),licenseId);
        return responseMessage;

    }

    @CircuitBreaker(name = "licenseService", fallbackMethod= "buildFallbackLicenseList")
    @Bulkhead(name = "bulkheadLicenseService", fallbackMethod= "buildFallbackLicenseList")
    @RateLimiter(name = "licenseService", fallbackMethod= "buildFallbackLicenseList")
    @Retry(name = "retryLicenseService", fallbackMethod= "buildFallbackLicenseList")
    public List<License> getLicensesByOrganization(String organizationId) throws TimeoutException {
        randomlyRunLong();
        return licenseRepository.findByOrganizationId(organizationId);
    }

    private void randomlyRunLong() throws TimeoutException{
        Random rand = new Random();
        int randomNum = rand.nextInt(3) + 1;
        if (randomNum==3) sleep();
    }
    private void sleep() throws TimeoutException{
        try {
            Thread.sleep(5000);
            throw new java.util.concurrent.TimeoutException();
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
    }

    private List<License> buildFallbackLicenseList(String organizationId, Throwable t){
        List<License> fallbackList = new ArrayList<>();
        License license = new License();
        license.setLicenseId("0000000-00-00000");
        license.setOrganizationId(organizationId);
        license.setProductName(
                "Sorry no licensing information currently available");
        fallbackList.add(license);
        return fallbackList;
    }
}
