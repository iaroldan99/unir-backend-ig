package com.uade.seminario.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uade.seminario.dto.SendMessageRequest;
import com.uade.seminario.service.InstagramMessageSenderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MessageController.class)
class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private InstagramMessageSenderService messageSenderService;

    @Test
    void sendMessage_Success() throws Exception {
        SendMessageRequest request = SendMessageRequest.builder()
                .recipientId("123456")
                .text("Test message")
                .build();

        when(messageSenderService.sendTextMessageSync(anyString(), anyString()))
                .thenReturn("{\"message_id\":\"msg_123\"}");

        mockMvc.perform(post("/api/messages/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.recipientId").value("123456"));
    }

    @Test
    void sendMessage_ValidationError_MissingRecipientId() throws Exception {
        SendMessageRequest request = SendMessageRequest.builder()
                .text("Test message")
                .build();

        mockMvc.perform(post("/api/messages/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void sendMessage_ValidationError_MissingText() throws Exception {
        SendMessageRequest request = SendMessageRequest.builder()
                .recipientId("123456")
                .build();

        mockMvc.perform(post("/api/messages/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void health_ReturnsOk() throws Exception {
        mockMvc.perform(get("/api/messages/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.service").value("Message Controller"));
    }
}

