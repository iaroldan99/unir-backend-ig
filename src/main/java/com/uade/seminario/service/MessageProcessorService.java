package com.uade.seminario.service;

import com.uade.seminario.model.IncomingMessage;
import com.uade.seminario.model.MessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Servicio para procesar mensajes entrantes de Instagram
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MessageProcessorService {

    /**
     * Procesa un mensaje entrante de Instagram
     */
    public void processIncomingMessage(IncomingMessage incomingMessage) {
        if (incomingMessage == null || incomingMessage.getEntry() == null) {
            log.warn("Mensaje entrante vacío o nulo");
            return;
        }

        // Iterar sobre cada entrada
        incomingMessage.getEntry().forEach(entry -> {
            if (entry.getMessaging() != null) {
                entry.getMessaging().forEach(messaging -> {
                    processMessagingEvent(messaging);
                });
            }
        });
    }

    /**
     * Procesa un evento de mensajería individual
     */
    private void processMessagingEvent(IncomingMessage.Messaging messaging) {
        if (messaging.getMessage() == null) {
            log.debug("Evento de mensajería sin mensaje (puede ser un evento de entrega/lectura)");
            return;
        }

        // Convertir a DTO normalizado
        MessageDTO messageDTO = convertToMessageDTO(messaging);
        
        log.info("Mensaje procesado: {}", messageDTO);
        
        // Aquí puedes:
        // 1. Guardar el mensaje en una base de datos
        // 2. Enviarlo a un sistema de mensajería (Kafka, RabbitMQ, etc.)
        // 3. Procesarlo con lógica de negocio
        // 4. Responder automáticamente si es necesario
        
        // Por ahora solo lo registramos
        logMessageDetails(messageDTO);
    }

    /**
     * Convierte un mensaje de Instagram al formato normalizado
     */
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

    /**
     * Mapea el tipo de adjunto de Instagram a nuestro enum
     */
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

    /**
     * Registra los detalles del mensaje
     */
    private void logMessageDetails(MessageDTO messageDTO) {
        log.info("═══════════════════════════════════════");
        log.info("📩 NUEVO MENSAJE DE INSTAGRAM");
        log.info("ID: {}", messageDTO.getMessageId());
        log.info("De: {}", messageDTO.getSenderId());
        log.info("Para: {}", messageDTO.getRecipientId());
        log.info("Tipo: {}", messageDTO.getType());
        log.info("Texto: {}", messageDTO.getText());
        log.info("Timestamp: {}", messageDTO.getTimestamp());
        log.info("═══════════════════════════════════════");
    }
}

