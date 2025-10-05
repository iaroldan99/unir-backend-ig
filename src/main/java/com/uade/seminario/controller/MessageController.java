package com.uade.seminario.controller;

import com.uade.seminario.service.InstagramMessageSenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controlador REST para enviar mensajes a Instagram
 */
@Slf4j
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final InstagramMessageSenderService messageSenderService;

    /**
     * Endpoint para enviar un mensaje de texto
     * 
     * POST /api/messages/send
     * Body: {
     *   "recipientId": "instagram_user_id",
     *   "text": "Hola desde la API"
     * }
     */
    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody Map<String, String> request) {
        try {
            String recipientId = request.get("recipientId");
            String text = request.get("text");

            if (recipientId == null || text == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "recipientId y text son requeridos"));
            }

            log.info("Solicitud para enviar mensaje a: {}", recipientId);
            
            String response = messageSenderService.sendTextMessageSync(recipientId, text);
            
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Mensaje enviado correctamente",
                    "response", response
            ));
            
        } catch (Exception e) {
            log.error("Error al enviar mensaje", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of(
                            "status", "error",
                            "message", e.getMessage()
                    ));
        }
    }

    /**
     * Health check del controlador
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "Message Controller"
        ));
    }
}

