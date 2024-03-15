package dev.leesama.openai;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

class OpenAIServiceTest {

    private OpenAIService openAIService;
    private WebClient webClientMock;
    private WebClient.RequestHeadersUriSpec requestHeadersUriMock;
    private WebClient.RequestBodySpec requestBodyMock;
    private WebClient.RequestHeadersSpec requestHeadersMock;
    private WebClient.ResponseSpec responseMock;
    private OpenAIProperties propertiesMock;

    @BeforeEach
    void setUp() {
        webClientMock = mock(WebClient.class);
        WebClient.RequestBodyUriSpec requestBodyUriMock = mock(WebClient.RequestBodyUriSpec.class);
        WebClient.RequestBodySpec requestBodyMock = mock(WebClient.RequestBodySpec.class);
        WebClient.RequestHeadersSpec requestHeadersMock = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseMock = mock(WebClient.ResponseSpec.class);

        when(webClientMock.post()).thenReturn(requestBodyUriMock);
        when(requestBodyUriMock.uri(any(Function.class))).thenReturn(requestBodyMock);
        when(requestBodyMock.header(anyString(), anyString())).thenReturn(requestBodyMock);
        when(requestBodyMock.bodyValue(any())).thenReturn(requestHeadersMock);
        when(requestHeadersMock.retrieve()).thenReturn(responseMock);
        when(responseMock.bodyToMono(String.class)).thenReturn(Mono.just("mocked response"));

        // Mock the OpenAIProperties and its ModelConfig
        propertiesMock = Mockito.mock(OpenAIProperties.class);
        OpenAIProperties.ModelConfig modelConfig = new OpenAIProperties.ModelConfig();
        modelConfig.setName("testModel");
        modelConfig.setMaxTokens(100);
        modelConfig.setTemperature(0.5);

        when(propertiesMock.getApiKey()).thenReturn("testApiKey");
        when(propertiesMock.getBaseUrl()).thenReturn("https://api.openai.com");
        when(propertiesMock.getModels()).thenReturn(java.util.Collections.singletonList(modelConfig));

        openAIService = new OpenAIService(webClientMock, propertiesMock);
    }

    @Test
    void callOpenAI() {
        String response = openAIService.callOpenAI("testModel", "Test prompt").block();

        // Verify that the webClientMock.post() method was called
        verify(webClientMock).post();

        // Assertions
        assertNotNull(response);
        assertEquals("mocked response", response);
    }
}
