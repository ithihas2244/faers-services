package com.faers.emergency.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class EmergencyEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(EmergencyEventPublisher.class);
    private final ApplicationEventPublisher applicationEventPublisher;

    public EmergencyEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publishEmergencyAccepted(EmergencyRequestAcceptedEvent event) {
        log.info("Publishing EmergencyRequestAcceptedEvent: id={}, type={}, severity={}, lat={}, lon={}",
                event.getEmergencyRequestId(),
                event.getEmergencyType(),
                event.getSeverity(),
                event.getLocation().getLatitude(),
                event.getLocation().getLongitude());

        // Publish to internal Spring application context (can be listened by messaging bridge or dispatch service)
        applicationEventPublisher.publishEvent(event);
    }
}
