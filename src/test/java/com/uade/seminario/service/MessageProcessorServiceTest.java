package com.uade.seminario.service;

import com.uade.seminario.model.IncomingMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MessageProcessorServiceTest {

    @InjectMocks
    private MessageProcessorService service;

    @Test
    void processIncomingMessage_Success() {
        IncomingMessage message = createTestMessage();
        
        assertDoesNotThrow(() -> service.processIncomingMessage(message));
    }

    @Test
    void processIncomingMessage_NullMessage() {
        assertDoesNotThrow(() -> service.processIncomingMessage(null));
    }

    @Test
    void processIncomingMessage_EmptyEntry() {
        IncomingMessage message = IncomingMessage.builder()
                .object("instagram")
                .entry(List.of())
                .build();
        
        assertDoesNotThrow(() -> service.processIncomingMessage(message));
    }

    private IncomingMessage createTestMessage() {
        IncomingMessage.Message msg = IncomingMessage.Message.builder()
                .mid("msg_123")
                .text("Test message")
                .build();

        IncomingMessage.Messaging messaging = IncomingMessage.Messaging.builder()
                .sender(IncomingMessage.Sender.builder().id("sender_123").build())
                .recipient(IncomingMessage.Recipient.builder().id("recipient_123").build())
                .timestamp(System.currentTimeMillis())
                .message(msg)
                .build();

        IncomingMessage.Entry entry = IncomingMessage.Entry.builder()
                .id("entry_123")
                .time(System.currentTimeMillis())
                .messaging(List.of(messaging))
                .build();

        return IncomingMessage.builder()
                .object("instagram")
                .entry(List.of(entry))
                .build();
    }
}

