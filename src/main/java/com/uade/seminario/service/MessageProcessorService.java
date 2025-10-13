package com.uade.seminario.service;

import com.uade.seminario.dto.MessageDTO;
import com.uade.seminario.model.IncomingMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageProcessorService {

    public void processIncomingMessage(IncomingMessage incomingMessage) {
        if (incomingMessage == null || incomingMessage.getEntry() == null) {
            log.warn("Empty or null incoming message");
            return;
        }

        incomingMessage.getEntry().forEach(entry -> {
            if (entry.getMessaging() != null) {
                entry.getMessaging().forEach(this::processMessagingEvent);
            }
        });
    }

    private void processMessagingEvent(IncomingMessage.Messaging messaging) {
        if (messaging.getMessage() == null) {
            log.debug("Messaging event without message");
            return;
        }

        MessageDTO messageDTO = convertToMessageDTO(messaging);
        log.info("Message processed: {}", messageDTO);
        logMessageDetails(messageDTO);
    }

    private MessageDTO convertToMessageDTO(IncomingMessage.Messaging messaging) {
        IncomingMessage.Message message = messaging.getMessage();
        
        MessageDTO.MessageType type = MessageDTO.MessageType.TEXT;
        if (message.getAttachments() != null && !message.getAttachments().isEmpty()) {
            String attachmentType = message.getAttachments().get(0).getType();
            type = mapAttachmentType(attachmentType);
        }

        return MessageDTO.builder()
                .messageId(message.getMid())
                .senderId(messaging.getSender().getId())
                .recipientId(messaging.getRecipient().getId())
                .text(message.getText())
                .timestamp(messaging.getTimestamp())
                .type(type)
                .platform("INSTAGRAM")
                .build();
    }

    private MessageDTO.MessageType mapAttachmentType(String attachmentType) {
        if (attachmentType == null) {
            return MessageDTO.MessageType.OTHER;
        }
        
        return switch (attachmentType.toLowerCase()) {
            case "image" -> MessageDTO.MessageType.IMAGE;
            case "video" -> MessageDTO.MessageType.VIDEO;
            case "audio" -> MessageDTO.MessageType.AUDIO;
            case "file" -> MessageDTO.MessageType.FILE;
            default -> MessageDTO.MessageType.OTHER;
        };
    }

    private void logMessageDetails(MessageDTO messageDTO) {
        log.info("═══════════════════════════════════════");
        log.info("📩 NEW INSTAGRAM MESSAGE");
        log.info("ID: {}", messageDTO.getMessageId());
        log.info("From: {}", messageDTO.getSenderId());
        log.info("To: {}", messageDTO.getRecipientId());
        log.info("Type: {}", messageDTO.getType());
        log.info("Text: {}", messageDTO.getText());
        log.info("Timestamp: {}", messageDTO.getTimestamp());
        log.info("═══════════════════════════════════════");
    }
}
