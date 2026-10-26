package com.faers.emergency;

import com.faers.emergency.controller.EmergencyRequestController;
import com.faers.emergency.dto.request.LocationDto;
import com.faers.emergency.dto.response.EmergencyResponseDto;
import com.faers.emergency.exception.GlobalExceptionHandler;
import com.faers.emergency.model.EmergencyStatus;
import com.faers.emergency.model.EmergencyType;
import com.faers.emergency.model.Severity;
import com.faers.emergency.service.EmergencyRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class EmergencyRequestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmergencyRequestService emergencyRequestService;

    @InjectMocks
    private EmergencyRequestController controller;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void testCreateEmergencyRequest_Success() throws Exception {
        EmergencyResponseDto response = new EmergencyResponseDto();
        response.setId("EMG-102401");
        response.setCallerId("CALL-1024");
        response.setEmergencyType(EmergencyType.MEDICAL);
        response.setSeverity(Severity.HIGH);
        response.setStatus(EmergencyStatus.DISPATCH_PENDING);
        response.setLocation(new LocationDto(13.0827, 80.2707, "Chennai Central", "Gate 2"));
        response.setDescription("Breathing difficulty");
        response.setCreatedAt(Instant.now());
        response.setUpdatedAt(Instant.now());

        when(emergencyRequestService.createEmergencyRequest(any())).thenReturn(response);

        String jsonPayload = """
                {
                    "callerId": "CALL-1024",
                    "emergencyType": "MEDICAL",
                    "severity": "HIGH",
                    "location": {
                        "latitude": 13.0827,
                        "longitude": 80.2707,
                        "address": "Chennai Central",
                        "landmark": "Gate 2"
                    },
                    "description": "Breathing difficulty"
                }
                """;

        mockMvc.perform(post("/api/v1/emergency-requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value("EMG-102401"))
                .andExpect(jsonPath("$.data.callerId").value("CALL-1024"))
                .andExpect(jsonPath("$.data.status").value("DISPATCH_PENDING"));
    }

    @Test
    void testCreateEmergencyRequest_ValidationFailure() throws Exception {
        String invalidPayload = "{}";

        mockMvc.perform(post("/api/v1/emergency-requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPayload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void testGetEmergencyRequestById_Success() throws Exception {
        EmergencyResponseDto response = new EmergencyResponseDto();
        response.setId("EMG-1001");
        response.setEmergencyType(EmergencyType.FIRE);
        response.setSeverity(Severity.CRITICAL);
        response.setStatus(EmergencyStatus.RECEIVED);

        when(emergencyRequestService.getEmergencyRequestById("EMG-1001")).thenReturn(response);

        mockMvc.perform(get("/api/v1/emergency-requests/EMG-1001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value("EMG-1001"));
    }
}
