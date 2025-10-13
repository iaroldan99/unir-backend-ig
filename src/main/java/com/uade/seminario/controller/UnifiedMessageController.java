package com.uade.seminario.controller;

import com.uade.seminario.dto.HealthResponse;
import com.uade.seminario.dto.SendMessageRequest;
import com.uade.seminario.dto.SendMessageResponse;
import com.uade.seminario.service.InstagramMessageSenderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/send")
@RequiredArgsConstructor
public class UnifiedMessageController {

    private final InstagramMessageSenderService messageSenderService;

    @PostMapping("/instagram")
    public ResponseEntity<SendMessageResponse> sendInstagramMessage(
            @Valid @RequestBody SendMessageRequest request) {
        
        log.info("Request to send Instagram message to: {}", request.getRecipientId());
        
        String messageId = messageSenderService.sendTextMessageSync(
            request.getRecipientId(), 
            request.getText()
        );
        
        SendMessageResponse response = SendMessageResponse.builder()
                .status("success")
                .message("Message sent successfully")
                .messageId(messageId)
                .recipientId(request.getRecipientId())
                .build();
                
        return ResponseEntity.ok(response);
    }

    @PostMapping("/instagram/{igUserId}/messages")
    public ResponseEntity<SendMessageResponse> sendInstagramMessageByUserId(
            @PathVariable String igUserId,
            @Valid @RequestBody SendMessageRequest request) {
        
        log.info("Request to send Instagram message to user: {}", igUserId);
        
        String messageId = messageSenderService.sendTextMessageSync(igUserId, request.getText());
        
        SendMessageResponse response = SendMessageResponse.builder()
                .status("success")
                .message("Message sent successfully")
                .messageId(messageId)
                .recipientId(igUserId)
                .build();
                
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<HealthResponse> health() {
        HealthResponse response = HealthResponse.builder()
                .status("UP")
                .service("Unified Message Controller")
                .version("1.0.0")
                .build();
                
        return ResponseEntity.ok(response);
    }
}
