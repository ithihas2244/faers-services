package com.faers.emergency.event;

import com.faers.emergency.dto.request.LocationDto;
import com.faers.emergency.model.EmergencyType;
import com.faers.emergency.model.Severity;

import java.time.Instant;

public class EmergencyRequestAcceptedEvent {

    private String emergencyRequestId;
    private String callerId;
    private EmergencyType emergencyType;
    private Severity severity;
    private LocationDto location;
    private String description;
    private Instant timestamp;

    public EmergencyRequestAcceptedEvent() {
    }

    public EmergencyRequestAcceptedEvent(String emergencyRequestId, String callerId, EmergencyType emergencyType,
                                         Severity severity, LocationDto location, String description, Instant timestamp) {
        this.emergencyRequestId = emergencyRequestId;
        this.callerId = callerId;
        this.emergencyType = emergencyType;
        this.severity = severity;
        this.location = location;
        this.description = description;
        this.timestamp = timestamp;
    }

    public String getEmergencyRequestId() {
        return emergencyRequestId;
    }

    public void setEmergencyRequestId(String emergencyRequestId) {
        this.emergencyRequestId = emergencyRequestId;
    }

    public String getCallerId() {
        return callerId;
    }

    public void setCallerId(String callerId) {
        this.callerId = callerId;
    }

    public EmergencyType getEmergencyType() {
        return emergencyType;
    }

    public void setEmergencyType(EmergencyType emergencyType) {
        this.emergencyType = emergencyType;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public LocationDto getLocation() {
        return location;
    }

    public void setLocation(LocationDto location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
