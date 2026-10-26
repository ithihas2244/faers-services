package com.faers.emergency.controller;

import com.faers.emergency.dto.request.CreateEmergencyRequestDto;
import com.faers.emergency.dto.response.ApiResponse;
import com.faers.emergency.dto.response.EmergencyResponseDto;
import com.faers.emergency.service.EmergencyRequestService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/webhooks")
public class WebhookController {

    private static final Logger log = LoggerFactory.getLogger(WebhookController.class);
    private final EmergencyRequestService emergencyRequestService;

    public WebhookController(EmergencyRequestService emergencyRequestService) {
        this.emergencyRequestService = emergencyRequestService;
    }

    @PostMapping({"/voice-agent", "/elevenlabs", "/exotel"})
    public ResponseEntity<ApiResponse<EmergencyResponseDto>> handleVoiceAgentWebhook(
            @RequestHeader(value = "X-Webhook-Secret", required = false) String webhookSecret,
            @Valid @RequestBody CreateEmergencyRequestDto requestDto) {

        log.info("Incoming voice agent webhook: callerId={}, type={}, severity={}",
                requestDto.getCallerId(), requestDto.getEmergencyType(), requestDto.getSeverity());

        EmergencyResponseDto response = emergencyRequestService.createEmergencyRequest(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Voice agent webhook received and emergency dispatched", response));
    }
}
