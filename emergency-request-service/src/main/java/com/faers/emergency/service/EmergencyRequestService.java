package com.faers.emergency.service;

import com.faers.emergency.dto.request.CreateEmergencyRequestDto;
import com.faers.emergency.dto.request.UpdateEmergencyStatusDto;
import com.faers.emergency.dto.response.EmergencyResponseDto;
import com.faers.emergency.model.EmergencyStatus;
import com.faers.emergency.model.Severity;

import java.util.List;

public interface EmergencyRequestService {

    EmergencyResponseDto createEmergencyRequest(CreateEmergencyRequestDto dto);

    EmergencyResponseDto getEmergencyRequestById(String id);

    List<EmergencyResponseDto> getAllEmergencyRequests(EmergencyStatus status, Severity severity);

    EmergencyResponseDto updateEmergencyStatus(String id, UpdateEmergencyStatusDto dto);

    EmergencyResponseDto cancelEmergencyRequest(String id, String reason);
}
