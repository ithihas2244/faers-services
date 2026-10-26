package com.faers.emergency;

import com.faers.emergency.dao.EmergencyRequestDao;
import com.faers.emergency.dto.request.CreateEmergencyRequestDto;
import com.faers.emergency.dto.request.LocationDto;
import com.faers.emergency.dto.request.UpdateEmergencyStatusDto;
import com.faers.emergency.dto.response.EmergencyResponseDto;
import com.faers.emergency.entity.EmergencyRequestEntity;
import com.faers.emergency.event.EmergencyEventPublisher;
import com.faers.emergency.exception.InvalidStatusTransitionException;
import com.faers.emergency.exception.ResourceNotFoundException;
import com.faers.emergency.model.EmergencyStatus;
import com.faers.emergency.model.EmergencyType;
import com.faers.emergency.model.Severity;
import com.faers.emergency.service.impl.EmergencyRequestServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmergencyRequestServiceTest {

    @Mock
    private EmergencyRequestDao emergencyRequestDao;

    @Mock
    private EmergencyEventPublisher eventPublisher;

    @InjectMocks
    private EmergencyRequestServiceImpl service;

    private CreateEmergencyRequestDto createDto;

    @BeforeEach
    void setUp() {
        createDto = new CreateEmergencyRequestDto(
                "CALL-1024",
                EmergencyType.MEDICAL,
                Severity.CRITICAL,
                new LocationDto(13.0827, 80.2707, "Central Station", "Platform 1"),
                "Severe heart discomfort"
        );
    }

    @Test
    void testCreateEmergencyRequest_Success() {
        when(emergencyRequestDao.save(any(EmergencyRequestEntity.class))).thenAnswer(invocation -> {
            EmergencyRequestEntity entity = invocation.getArgument(0);
            entity.setCreatedAt(Instant.now());
            entity.setUpdatedAt(Instant.now());
            return entity;
        });

        EmergencyResponseDto response = service.createEmergencyRequest(createDto);

        assertNotNull(response);
        assertNotNull(response.getId());
        assertTrue(response.getId().startsWith("EMG-"));
        assertEquals("CALL-1024", response.getCallerId());
        assertEquals(EmergencyType.MEDICAL, response.getEmergencyType());
        assertEquals(Severity.CRITICAL, response.getSeverity());
        assertEquals(EmergencyStatus.DISPATCH_PENDING, response.getStatus());
        assertEquals(13.0827, response.getLocation().getLatitude());

        verify(emergencyRequestDao, times(1)).save(any(EmergencyRequestEntity.class));
        verify(eventPublisher, times(1)).publishEmergencyAccepted(any());
    }

    @Test
    void testGetEmergencyRequestById_NotFound() {
        when(emergencyRequestDao.findById("EMG-9999")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getEmergencyRequestById("EMG-9999"));
    }

    @Test
    void testUpdateStatus_CannotUpdateCancelled() {
        EmergencyRequestEntity entity = new EmergencyRequestEntity();
        entity.setId("EMG-1001");
        entity.setStatus(EmergencyStatus.CANCELLED);

        when(emergencyRequestDao.findById("EMG-1001")).thenReturn(Optional.of(entity));

        UpdateEmergencyStatusDto updateDto = new UpdateEmergencyStatusDto(EmergencyStatus.DISPATCHED, "AMB-01", "ASN-01", null);

        assertThrows(InvalidStatusTransitionException.class, () -> service.updateEmergencyStatus("EMG-1001", updateDto));
    }

    @Test
    void testCancelEmergency_Success() {
        EmergencyRequestEntity entity = new EmergencyRequestEntity();
        entity.setId("EMG-1001");
        entity.setStatus(EmergencyStatus.DISPATCH_PENDING);
        entity.setDescription("Patient collapsed");
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());

        when(emergencyRequestDao.findById("EMG-1001")).thenReturn(Optional.of(entity));
        when(emergencyRequestDao.save(any(EmergencyRequestEntity.class))).thenAnswer(i -> i.getArgument(0));

        EmergencyResponseDto cancelled = service.cancelEmergencyRequest("EMG-1001", "Caller cancelled");

        assertEquals(EmergencyStatus.CANCELLED, cancelled.getStatus());
        assertTrue(cancelled.getDescription().contains("CANCELLED: Caller cancelled"));
    }
}
