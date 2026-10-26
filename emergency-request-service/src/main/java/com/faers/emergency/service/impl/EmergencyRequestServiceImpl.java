package com.faers.emergency.service.impl;

import com.faers.emergency.dao.EmergencyRequestDao;
import com.faers.emergency.dto.request.CreateEmergencyRequestDto;
import com.faers.emergency.dto.request.LocationDto;
import com.faers.emergency.dto.request.UpdateEmergencyStatusDto;
import com.faers.emergency.dto.response.EmergencyResponseDto;
import com.faers.emergency.entity.EmergencyRequestEntity;
import com.faers.emergency.event.EmergencyEventPublisher;
import com.faers.emergency.event.EmergencyRequestAcceptedEvent;
import com.faers.emergency.exception.InvalidStatusTransitionException;
import com.faers.emergency.exception.ResourceNotFoundException;
import com.faers.emergency.model.EmergencyStatus;
import com.faers.emergency.model.Severity;
import com.faers.emergency.service.EmergencyRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class EmergencyRequestServiceImpl implements EmergencyRequestService {

    private static final Logger log = LoggerFactory.getLogger(EmergencyRequestServiceImpl.class);

    private final EmergencyRequestDao emergencyRequestDao;
    private final EmergencyEventPublisher eventPublisher;

    public EmergencyRequestServiceImpl(EmergencyRequestDao emergencyRequestDao,
                                      EmergencyEventPublisher eventPublisher) {
        this.emergencyRequestDao = emergencyRequestDao;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public EmergencyResponseDto createEmergencyRequest(CreateEmergencyRequestDto dto) {
        log.info("Received emergency request: callerId={}, type={}, severity={}",
                dto.getCallerId(), dto.getEmergencyType(), dto.getSeverity());

        String id = generateEmergencyId();

        EmergencyRequestEntity entity = new EmergencyRequestEntity();
        entity.setId(id);
        entity.setCallerId(dto.getCallerId());
        entity.setEmergencyType(dto.getEmergencyType());
        entity.setSeverity(dto.getSeverity());
        entity.setLatitude(dto.getLocation().getLatitude());
        entity.setLongitude(dto.getLocation().getLongitude());
        entity.setAddress(dto.getLocation().getAddress());
        entity.setLandmark(dto.getLocation().getLandmark());
        entity.setDescription(dto.getDescription());
        entity.setStatus(EmergencyStatus.DISPATCH_PENDING);

        EmergencyRequestEntity saved = emergencyRequestDao.save(entity);
        log.info("Emergency request saved successfully with ID: {}", saved.getId());

        // Publish accepted event to notify dispatch workflow
        EmergencyRequestAcceptedEvent event = new EmergencyRequestAcceptedEvent(
                saved.getId(),
                saved.getCallerId(),
                saved.getEmergencyType(),
                saved.getSeverity(),
                dto.getLocation(),
                saved.getDescription(),
                Instant.now()
        );
        eventPublisher.publishEmergencyAccepted(event);

        return mapToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public EmergencyResponseDto getEmergencyRequestById(String id) {
        EmergencyRequestEntity entity = emergencyRequestDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Emergency request not found with ID: " + id));
        return mapToDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmergencyResponseDto> getAllEmergencyRequests(EmergencyStatus status, Severity severity) {
        List<EmergencyRequestEntity> list;
        if (status != null) {
            list = emergencyRequestDao.findByStatus(status);
        } else if (severity != null) {
            list = emergencyRequestDao.findBySeverity(severity);
        } else {
            list = emergencyRequestDao.findAll();
        }

        return list.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EmergencyResponseDto updateEmergencyStatus(String id, UpdateEmergencyStatusDto dto) {
        EmergencyRequestEntity entity = emergencyRequestDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Emergency request not found with ID: " + id));

        // Validate state transitions
        if (entity.getStatus() == EmergencyStatus.COMPLETED) {
            throw new InvalidStatusTransitionException("Cannot update completed emergency request: " + id);
        }
        if (entity.getStatus() == EmergencyStatus.CANCELLED) {
            throw new InvalidStatusTransitionException("Cannot update cancelled emergency request: " + id);
        }

        entity.setStatus(dto.getStatus());
        if (dto.getVehicleId() != null) {
            entity.setAssignedVehicleId(dto.getVehicleId());
        }
        if (dto.getAssignmentId() != null) {
            entity.setAssignedAssignmentId(dto.getAssignmentId());
        }

        EmergencyRequestEntity updated = emergencyRequestDao.save(entity);
        log.info("Updated status of emergency {}: newStatus={}, vehicleId={}",
                id, updated.getStatus(), updated.getAssignedVehicleId());
        return mapToDto(updated);
    }

    @Override
    @Transactional
    public EmergencyResponseDto cancelEmergencyRequest(String id, String reason) {
        EmergencyRequestEntity entity = emergencyRequestDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Emergency request not found with ID: " + id));

        if (entity.getStatus() == EmergencyStatus.COMPLETED) {
            throw new InvalidStatusTransitionException("Cannot cancel an already completed emergency request: " + id);
        }

        entity.setStatus(EmergencyStatus.CANCELLED);
        if (reason != null && !reason.isBlank()) {
            String currentDesc = entity.getDescription() != null ? entity.getDescription() : "";
            entity.setDescription(currentDesc + " [CANCELLED: " + reason + "]");
        }

        EmergencyRequestEntity updated = emergencyRequestDao.save(entity);
        log.warn("Emergency {} cancelled. Reason: {}", id, reason);
        return mapToDto(updated);
    }

    private String generateEmergencyId() {
        // e.g. EMG-104829
        long timestamp = System.currentTimeMillis() % 1000000;
        int random = ThreadLocalRandom.current().nextInt(100, 999);
        return String.format("EMG-%d%d", timestamp, random);
    }

    private EmergencyResponseDto mapToDto(EmergencyRequestEntity entity) {
        EmergencyResponseDto dto = new EmergencyResponseDto();
        dto.setId(entity.getId());
        dto.setCallerId(entity.getCallerId());
        dto.setEmergencyType(entity.getEmergencyType());
        dto.setSeverity(entity.getSeverity());
        dto.setLocation(new LocationDto(
                entity.getLatitude(),
                entity.getLongitude(),
                entity.getAddress(),
                entity.getLandmark()
        ));
        dto.setDescription(entity.getDescription());
        dto.setStatus(entity.getStatus());
        dto.setAssignedVehicleId(entity.getAssignedVehicleId());
        dto.setAssignedAssignmentId(entity.getAssignedAssignmentId());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}
