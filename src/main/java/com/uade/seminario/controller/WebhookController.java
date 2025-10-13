package com.uade.seminario.controller;

import com.uade.seminario.config.InstagramConfig;
import com.uade.seminario.dto.HealthResponse;
import com.uade.seminario.model.IncomingMessage;
import com.uade.seminario.service.MessageProcessorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/webhook/instagram")
@RequiredArgsConstructor
public class WebhookController {

    private final InstagramConfig instagramConfig;
    private final MessageProcessorService messageProcessorService;

    @GetMapping
    public ResponseEntity<String> verifyWebhook(
            @RequestParam("hub.mode") String mode,
            @RequestParam("hub.verify_token") String token,
            @RequestParam("hub.challenge") String challenge) {
        
        log.info("Webhook verification request received");
        log.debug("Mode: {}, Token: {}", mode, token);

        if ("subscribe".equals(mode) && token.equals(instagramConfig.getVerifyToken())) {
            log.info("Webhook verified successfully");
            return ResponseEntity.ok(challenge);
        }
        
        log.error("Webhook verification failed - invalid token");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid verification token");
    }

    @PostMapping
    public ResponseEntity<String> receiveMessage(@RequestBody IncomingMessage incomingMessage) {
        try {
            log.info("Message received from Instagram: {}", incomingMessage);
            messageProcessorService.processIncomingMessage(incomingMessage);
            return ResponseEntity.ok("EVENT_RECEIVED");
        } catch (Exception e) {
            log.error("Error processing Instagram message", e);
            return ResponseEntity.ok("EVENT_RECEIVED");
        }
    }

    @GetMapping("/health")
    public ResponseEntity<HealthResponse> health() {
        HealthResponse response = HealthResponse.builder()
                .status("UP")
                .service("Webhook Controller")
                .version("1.0.0")
                .build();
                
        return ResponseEntity.ok(response);
    }
}
