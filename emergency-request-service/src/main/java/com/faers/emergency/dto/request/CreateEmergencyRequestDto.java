package com.faers.emergency.dto.request;

import com.faers.emergency.model.EmergencyType;
import com.faers.emergency.model.Severity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateEmergencyRequestDto {

    private String callerId;

    @NotNull(message = "Emergency type is required")
    private EmergencyType emergencyType;

    @NotNull(message = "Severity is required")
    private Severity severity;

    @NotNull(message = "Location is required")
    @Valid
    private LocationDto location;

    @NotBlank(message = "Description is required")
    private String description;

    public CreateEmergencyRequestDto() {
    }

    public CreateEmergencyRequestDto(String callerId, EmergencyType emergencyType, Severity severity, LocationDto location, String description) {
        this.callerId = callerId;
        this.emergencyType = emergencyType;
        this.severity = severity;
        this.location = location;
        this.description = description;
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
}
