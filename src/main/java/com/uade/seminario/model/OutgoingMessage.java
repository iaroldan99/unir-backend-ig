package com.uade.seminario.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * Modelo para mensajes salientes hacia Instagram
 */
@Data
@Builder
public class OutgoingMessage {

    @JsonProperty("recipient")
    private Recipient recipient;

    @JsonProperty("message")
    private Message message;

    @Data
    @Builder
    public static class Recipient {
        private String id;
    }

    @Data
    @Builder
    public static class Message {
        private String text;
    }
}

