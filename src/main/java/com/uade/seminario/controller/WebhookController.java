package com.uade.seminario.controller;

import com.uade.seminario.config.InstagramConfig;
import com.uade.seminario.model.IncomingMessage;
import com.uade.seminario.service.MessageProcessorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para manejar webhooks de Instagram
 */
@Slf4j
@RestController
@RequestMapping("/webhook/instagram")
@RequiredArgsConstructor
public class WebhookController {

    private final InstagramConfig instagramConfig;
    private final MessageProcessorService messageProcessorService;

    /**
     * Endpoint de verificación del webhook (GET)
     * Meta enviará una solicitud GET para verificar el webhook
     */
    @GetMapping
    public ResponseEntity<String> verifyWebhook(
            @RequestParam("hub.mode") String mode,
            @RequestParam("hub.verify_token") String token,
            @RequestParam("hub.challenge") String challenge) {
        
        log.info("Recibida solicitud de verificación del webhook");
        log.debug("Mode: {}, Token: {}", mode, token);

        // Verificar que el token coincida con el configurado
        if ("subscribe".equals(mode) && token.equals(instagramConfig.getVerifyToken())) {
            log.info("Webhook verificado exitosamente");
            return ResponseEntity.ok(challenge);
        } else {
            log.error("Fallo en la verificación del webhook. Token inválido.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token de verificación inválido");
        }
    }

    /**
     * Endpoint para recibir mensajes (POST)
     * Instagram enviará los mensajes a este endpoint
     */
    @PostMapping
    public ResponseEntity<String> receiveMessage(@RequestBody IncomingMessage incomingMessage) {
        try {
            log.info("Mensaje recibido de Instagram: {}", incomingMessage);
            
            // Procesar el mensaje
            messageProcessorService.processIncomingMessage(incomingMessage);
            
            // Responder rápidamente a Instagram (requerido)
            return ResponseEntity.ok("EVENT_RECEIVED");
            
        } catch (Exception e) {
            log.error("Error procesando mensaje de Instagram", e);
            // Aún así retornar 200 para que Instagram no reintente
            return ResponseEntity.ok("EVENT_RECEIVED");
        }
    }

    /**
     * Endpoint de health check
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Webhook activo y funcionando");
    }
}

