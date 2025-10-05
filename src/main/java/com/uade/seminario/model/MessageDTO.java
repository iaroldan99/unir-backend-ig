package com.uade.seminario.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para representar un mensaje normalizado en el sistema
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {
    
    private String messageId;
    private String senderId;
    private String recipientId;
    private String text;
    private Long timestamp;
    private MessageType type;
    private String platform;

    public enum MessageType {
        TEXT,
        IMAGE,
        VIDEO,
        AUDIO,
        FILE,
        OTHER
    }
}

