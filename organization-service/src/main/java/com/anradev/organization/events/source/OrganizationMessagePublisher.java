package com.anradev.organization.events.source;

import com.anradev.organization.events.model.OrganizationChangeModel;
import com.anradev.organization.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrganizationMessagePublisher {

    @Autowired
    StreamBridge streamBridge;

    public void publishOrganizationChange(String action, String organizationId){
        log.debug("Sending Kafka message {} for Organization Id: {}", action, organizationId);
        OrganizationChangeModel change =  new OrganizationChangeModel(
                OrganizationChangeModel.class.getTypeName(),
                action,
                organizationId,
                UserContext.getCorrelationId());

        streamBridge.send("output", MessageBuilder.withPayload(change).build());
    }
}