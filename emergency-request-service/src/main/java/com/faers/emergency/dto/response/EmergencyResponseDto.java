package com.faers.emergency.dto.response;

import com.faers.emergency.dto.request.LocationDto;
import com.faers.emergency.model.EmergencyStatus;
import com.faers.emergency.model.EmergencyType;
import com.faers.emergency.model.Severity;
import java.time.Instant;

public class EmergencyResponseDto {

    private String id;
    private String callerId;
    private EmergencyType emergencyType;
    private Severity severity;
    private LocationDto location;
    private String description;
    private EmergencyStatus status;
    private String assignedVehicleId;
    private String assignedAssignmentId;
    private Instant createdAt;
    private Instant updatedAt;

    public EmergencyResponseDto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public EmergencyStatus getStatus() {
        return status;
    }

    public void setStatus(EmergencyStatus status) {
        this.status = status;
    }

    public String getAssignedVehicleId() {
        return assignedVehicleId;
    }

    public void setAssignedVehicleId(String assignedVehicleId) {
        this.assignedVehicleId = assignedVehicleId;
    }

    public String getAssignedAssignmentId() {
        return assignedAssignmentId;
    }

    public void setAssignedAssignmentId(String assignedAssignmentId) {
        this.assignedAssignmentId = assignedAssignmentId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
