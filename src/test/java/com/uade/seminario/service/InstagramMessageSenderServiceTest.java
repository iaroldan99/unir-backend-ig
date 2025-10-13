package com.uade.seminario.service;

import com.uade.seminario.config.InstagramConfig;
import com.uade.seminario.exception.InstagramApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InstagramMessageSenderServiceTest {

    @Mock
    private WebClient webClient;

    @Mock
    private InstagramConfig instagramConfig;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private WebClient.RequestBodySpec requestBodySpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private InstagramMessageSenderService service;

    @BeforeEach
    void setUp() {
        when(instagramConfig.getAccessToken()).thenReturn("test-token");
    }

    @Test
    void sendTextMessage_Success() {
        String recipientId = "123456";
        String text = "Test message";
        String expectedResponse = "{\"message_id\":\"msg_123\"}";

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just(expectedResponse));

        Mono<String> result = service.sendTextMessage(recipientId, text);

        assertNotNull(result);
        assertEquals(expectedResponse, result.block());
        verify(webClient).post();
    }

    @Test
    void sendTextMessageSync_Success() {
        String recipientId = "123456";
        String text = "Test message";
        String expectedResponse = "{\"message_id\":\"msg_123\"}";

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just(expectedResponse));

        String result = service.sendTextMessageSync(recipientId, text);

        assertNotNull(result);
        assertEquals(expectedResponse, result);
    }

    @Test
    void sendTextMessageSync_ThrowsInstagramApiException() {
        String recipientId = "123456";
        String text = "Test message";

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class))
                .thenReturn(Mono.error(new RuntimeException("API Error")));

        assertThrows(InstagramApiException.class, 
            () -> service.sendTextMessageSync(recipientId, text));
    }
}

