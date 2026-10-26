package com.faers.emergency.dto.request;

import com.faers.emergency.model.EmergencyStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateEmergencyStatusDto {

    @NotNull(message = "Status is required")
    private EmergencyStatus status;

    private String vehicleId;
    private String assignmentId;
    private String remarks;

    public UpdateEmergencyStatusDto() {
    }

    public UpdateEmergencyStatusDto(EmergencyStatus status, String vehicleId, String assignmentId, String remarks) {
        this.status = status;
        this.vehicleId = vehicleId;
        this.assignmentId = assignmentId;
        this.remarks = remarks;
    }

    public EmergencyStatus getStatus() {
        return status;
    }

    public void setStatus(EmergencyStatus status) {
        this.status = status;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(String assignmentId) {
        this.assignmentId = assignmentId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
