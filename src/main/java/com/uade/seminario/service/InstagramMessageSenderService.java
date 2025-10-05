package com.uade.seminario.service;

import com.uade.seminario.config.InstagramConfig;
import com.uade.seminario.model.OutgoingMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Servicio para enviar mensajes a Instagram
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InstagramMessageSenderService {

    private final WebClient webClient;
    private final InstagramConfig instagramConfig;

    /**
     * Envía un mensaje de texto a un usuario de Instagram
     * 
     * @param recipientId ID del usuario destinatario
     * @param text Texto del mensaje
     * @return Respuesta de la API de Instagram
     */
    public Mono<String> sendTextMessage(String recipientId, String text) {
        log.info("Enviando mensaje a usuario: {}", recipientId);
        
        // Construir el mensaje
        OutgoingMessage message = OutgoingMessage.builder()
                .recipient(OutgoingMessage.Recipient.builder()
                        .id(recipientId)
                        .build())
                .message(OutgoingMessage.Message.builder()
                        .text(text)
                        .build())
                .build();

        // Endpoint de Instagram para enviar mensajes
        String endpoint = String.format("/me/messages?access_token=%s", 
                instagramConfig.getAccessToken());

        return webClient.post()
                .uri(endpoint)
                .bodyValue(message)
                .retrieve()
                .bodyToMono(String.class)
                .doOnSuccess(response -> {
                    log.info("Mensaje enviado exitosamente: {}", response);
                })
                .doOnError(error -> {
                    log.error("Error al enviar mensaje", error);
                });
    }

    /**
     * Envía un mensaje de texto de forma síncrona
     */
    public String sendTextMessageSync(String recipientId, String text) {
        try {
            return sendTextMessage(recipientId, text).block();
        } catch (Exception e) {
            log.error("Error al enviar mensaje de forma síncrona", e);
            throw new RuntimeException("Error al enviar mensaje", e);
        }
    }
}

