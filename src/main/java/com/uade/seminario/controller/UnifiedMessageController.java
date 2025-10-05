package com.uade.seminario.controller;

import com.uade.seminario.service.InstagramMessageSenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controlador unificado para envío de mensajes a diferentes plataformas
 * Este controlador sigue la especificación del proyecto de mensajería unificada
 * 
 * Endpoints:
 * - POST /send/instagram - Enviar mensaje a Instagram
 * - POST /send/whatsapp  - Enviar mensaje a WhatsApp (futuro)
 * - POST /send/gmail     - Enviar mensaje a Gmail (futuro)
 */
@Slf4j
@RestController
@RequestMapping("/send")
@RequiredArgsConstructor
public class UnifiedMessageController {

    private final InstagramMessageSenderService messageSenderService;

    /**
     * Endpoint para enviar un mensaje a Instagram
     * Especificación: POST /{IG_USER_ID}/messages con recipient y message
     * 
     * POST /send/instagram
     * Body: {
     *   "recipient": "instagram_user_id",
     *   "message": "Texto del mensaje"
     * }
     * 
     * O también soporta el formato alternativo:
     * Body: {
     *   "recipientId": "instagram_user_id",
     *   "text": "Texto del mensaje"
     * }
     */
    @PostMapping("/instagram")
    public ResponseEntity<?> sendInstagramMessage(@RequestBody Map<String, Object> request) {
        try {
            // Soportar ambos formatos: recipient/message y recipientId/text
            String recipientId = getRecipientId(request);
            String messageText = getMessageText(request);

            if (recipientId == null || recipientId.isBlank()) {
                return ResponseEntity.badRequest()
                        .body(Map.of(
                                "error", "recipient o recipientId es requerido",
                                "status", "error"
                        ));
            }

            if (messageText == null || messageText.isBlank()) {
                return ResponseEntity.badRequest()
                        .body(Map.of(
                                "error", "message o text es requerido",
                                "status", "error"
                        ));
            }

            log.info("Solicitud para enviar mensaje de Instagram a: {}", recipientId);
            
            String response = messageSenderService.sendTextMessageSync(recipientId, messageText);
            
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "platform", "instagram",
                    "message", "Mensaje enviado correctamente",
                    "recipientId", recipientId,
                    "response", response
            ));
            
        } catch (Exception e) {
            log.error("Error al enviar mensaje de Instagram", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of(
                            "status", "error",
                            "platform", "instagram",
                            "message", e.getMessage()
                    ));
        }
    }

    /**
     * Endpoint específico que sigue el formato de Instagram Graph API
     * POST /send/instagram/{igUserId}/messages
     */
    @PostMapping("/instagram/{igUserId}/messages")
    public ResponseEntity<?> sendInstagramMessageByUserId(
            @PathVariable String igUserId,
            @RequestBody Map<String, Object> request) {
        try {
            String messageText = getMessageText(request);

            if (messageText == null || messageText.isBlank()) {
                return ResponseEntity.badRequest()
                        .body(Map.of(
                                "error", "message o text es requerido",
                                "status", "error"
                        ));
            }

            log.info("Solicitud para enviar mensaje de Instagram a usuario: {}", igUserId);
            
            String response = messageSenderService.sendTextMessageSync(igUserId, messageText);
            
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "platform", "instagram",
                    "message", "Mensaje enviado correctamente",
                    "recipientId", igUserId,
                    "response", response
            ));
            
        } catch (Exception e) {
            log.error("Error al enviar mensaje de Instagram a usuario {}", igUserId, e);
            return ResponseEntity.internalServerError()
                    .body(Map.of(
                            "status", "error",
                            "platform", "instagram",
                            "message", e.getMessage()
                    ));
        }
    }

    /**
     * Health check del controlador unificado
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "Unified Message Controller",
                "platforms", "instagram,whatsapp,gmail"
        ));
    }

    // ═══════════════════════════════════════════════════════
    // Métodos auxiliares
    // ═══════════════════════════════════════════════════════

    /**
     * Extrae el recipientId de diferentes formatos de request
     */
    private String getRecipientId(Map<String, Object> request) {
        // Intentar con "recipient" primero (formato estándar)
        Object recipient = request.get("recipient");
        if (recipient instanceof String) {
            return (String) recipient;
        }
        
        // Si recipient es un objeto con "id"
        if (recipient instanceof Map) {
            Object id = ((Map<?, ?>) recipient).get("id");
            if (id instanceof String) {
                return (String) id;
            }
        }
        
        // Fallback a "recipientId" (formato alternativo)
        Object recipientId = request.get("recipientId");
        if (recipientId instanceof String) {
            return (String) recipientId;
        }
        
        return null;
    }

    /**
     * Extrae el mensaje de diferentes formatos de request
     */
    private String getMessageText(Map<String, Object> request) {
        // Intentar con "message" primero (formato estándar)
        Object message = request.get("message");
        if (message instanceof String) {
            return (String) message;
        }
        
        // Si message es un objeto con "text"
        if (message instanceof Map) {
            Object text = ((Map<?, ?>) message).get("text");
            if (text instanceof String) {
                return (String) text;
            }
        }
        
        // Fallback a "text" directamente (formato alternativo)
        Object text = request.get("text");
        if (text instanceof String) {
            return (String) text;
        }
        
        return null;
    }
}


