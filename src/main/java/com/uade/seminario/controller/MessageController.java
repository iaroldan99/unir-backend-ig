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
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final InstagramMessageSenderService messageSenderService;

    @PostMapping("/send")
    public ResponseEntity<SendMessageResponse> sendMessage(@Valid @RequestBody SendMessageRequest request) {
        log.info("Request to send message to: {}", request.getRecipientId());
        
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

    @GetMapping("/health")
    public ResponseEntity<HealthResponse> health() {
        HealthResponse response = HealthResponse.builder()
                .status("UP")
                .service("Message Controller")
                .version("1.0.0")
                .build();
                
        return ResponseEntity.ok(response);
    }
}
