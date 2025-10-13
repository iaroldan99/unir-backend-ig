package com.uade.seminario.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uade.seminario.config.InstagramConfig;
import com.uade.seminario.model.IncomingMessage;
import com.uade.seminario.service.MessageProcessorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WebhookController.class)
class WebhookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private InstagramConfig instagramConfig;

    @MockBean
    private MessageProcessorService messageProcessorService;

    @Test
    void verifyWebhook_Success() throws Exception {
        String verifyToken = "test-token";
        String challenge = "test-challenge";

        when(instagramConfig.getVerifyToken()).thenReturn(verifyToken);

        mockMvc.perform(get("/webhook/instagram")
                        .param("hub.mode", "subscribe")
                        .param("hub.verify_token", verifyToken)
                        .param("hub.challenge", challenge))
                .andExpect(status().isOk())
                .andExpect(content().string(challenge));
    }

    @Test
    void verifyWebhook_InvalidToken() throws Exception {
        when(instagramConfig.getVerifyToken()).thenReturn("correct-token");

        mockMvc.perform(get("/webhook/instagram")
                        .param("hub.mode", "subscribe")
                        .param("hub.verify_token", "wrong-token")
                        .param("hub.challenge", "challenge"))
                .andExpect(status().isForbidden());
    }

    @Test
    void receiveMessage_Success() throws Exception {
        IncomingMessage message = createTestMessage();
        
        doNothing().when(messageProcessorService).processIncomingMessage(any());

        mockMvc.perform(post("/webhook/instagram")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(message)))
                .andExpect(status().isOk())
                .andExpect(content().string("EVENT_RECEIVED"));
    }

    @Test
    void health_ReturnsOk() throws Exception {
        mockMvc.perform(get("/webhook/instagram/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
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

