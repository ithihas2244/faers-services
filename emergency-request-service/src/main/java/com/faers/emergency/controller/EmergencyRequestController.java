package com.faers.emergency.controller;

import com.faers.emergency.dto.request.CreateEmergencyRequestDto;
import com.faers.emergency.dto.request.UpdateEmergencyStatusDto;
import com.faers.emergency.dto.response.ApiResponse;
import com.faers.emergency.dto.response.EmergencyResponseDto;
import com.faers.emergency.model.EmergencyStatus;
import com.faers.emergency.model.Severity;
import com.faers.emergency.service.EmergencyRequestService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/emergency-requests")
public class EmergencyRequestController {

    private final EmergencyRequestService emergencyRequestService;

    public EmergencyRequestController(EmergencyRequestService emergencyRequestService) {
        this.emergencyRequestService = emergencyRequestService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EmergencyResponseDto>> createEmergencyRequest(
            @Valid @RequestBody CreateEmergencyRequestDto requestDto) {
        EmergencyResponseDto response = emergencyRequestService.createEmergencyRequest(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Emergency request accepted and queued for dispatch", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EmergencyResponseDto>> getEmergencyRequestById(@PathVariable String id) {
        EmergencyResponseDto response = emergencyRequestService.getEmergencyRequestById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<EmergencyResponseDto>>> getAllEmergencyRequests(
            @RequestParam(required = false) EmergencyStatus status,
            @RequestParam(required = false) Severity severity) {
        List<EmergencyResponseDto> list = emergencyRequestService.getAllEmergencyRequests(status, severity);
        return ResponseEntity.ok(ApiResponse.success(list));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<EmergencyResponseDto>> updateStatus(
            @PathVariable String id,
            @Valid @RequestBody UpdateEmergencyStatusDto updateDto) {
        EmergencyResponseDto response = emergencyRequestService.updateEmergencyStatus(id, updateDto);
        return ResponseEntity.ok(ApiResponse.success("Emergency status updated successfully", response));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<EmergencyResponseDto>> cancelEmergency(
            @PathVariable String id,
            @RequestParam(required = false, defaultValue = "Cancelled by user/operator") String reason) {
        EmergencyResponseDto response = emergencyRequestService.cancelEmergencyRequest(id, reason);
        return ResponseEntity.ok(ApiResponse.success("Emergency request cancelled", response));
    }
}
