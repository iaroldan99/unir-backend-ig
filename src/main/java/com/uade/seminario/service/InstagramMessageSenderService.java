package com.uade.seminario.service;

import com.uade.seminario.config.InstagramConfig;
import com.uade.seminario.exception.InstagramApiException;
import com.uade.seminario.model.OutgoingMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class InstagramMessageSenderService {

    private final WebClient webClient;
    private final InstagramConfig instagramConfig;

    public Mono<String> sendTextMessage(String recipientId, String text) {
        log.info("Sending message to user: {}", recipientId);
        
        OutgoingMessage message = OutgoingMessage.builder()
                .recipient(OutgoingMessage.Recipient.builder()
                        .id(recipientId)
                        .build())
                .message(OutgoingMessage.Message.builder()
                        .text(text)
                        .build())
                .build();

        String endpoint = "/me/messages?access_token=" + instagramConfig.getAccessToken();

        return webClient.post()
                .uri(endpoint)
                .bodyValue(message)
                .retrieve()
                .bodyToMono(String.class)
                .doOnSuccess(response -> log.info("Message sent successfully: {}", response))
                .doOnError(error -> log.error("Error sending message", error));
    }

    public String sendTextMessageSync(String recipientId, String text) {
        try {
            return sendTextMessage(recipientId, text).block();
        } catch (Exception e) {
            log.error("Error sending message synchronously", e);
            throw new InstagramApiException("Failed to send message to Instagram", e);
        }
    }
}
