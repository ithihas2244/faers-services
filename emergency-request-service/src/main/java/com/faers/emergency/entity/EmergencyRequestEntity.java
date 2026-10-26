package com.faers.emergency.entity;

import com.faers.emergency.model.EmergencyStatus;
import com.faers.emergency.model.EmergencyType;
import com.faers.emergency.model.Severity;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "emergency_requests")
public class EmergencyRequestEntity {

    @Id
    @Column(name = "id", length = 64, nullable = false)
    private String id;

    @Column(name = "caller_id", length = 100)
    private String callerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "emergency_type", length = 50, nullable = false)
    private EmergencyType emergencyType;

    @Enumerated(EnumType.STRING)
    @Column(name = "severity", length = 50, nullable = false)
    private Severity severity;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "landmark", length = 255)
    private String landmark;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50, nullable = false)
    private EmergencyStatus status = EmergencyStatus.RECEIVED;

    @Column(name = "assigned_vehicle_id", length = 64)
    private String assignedVehicleId;

    @Column(name = "assigned_assignment_id", length = 64)
    private String assignedAssignmentId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public EmergencyRequestEntity() {
    }

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        if (createdAt == null) {
            createdAt = now;
        }
        if (updatedAt == null) {
            updatedAt = now;
        }
        if (status == null) {
            status = EmergencyStatus.RECEIVED;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    // Getters and Setters

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

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
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
