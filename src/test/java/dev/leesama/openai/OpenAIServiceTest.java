package dev.leesama.openai;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OpenAIServiceTest {

    private OpenAIService openAIService;
    private WebClient webClientMock;
    private WebClient.RequestHeadersSpec requestHeadersMock;
    private WebClient.RequestHeadersUriSpec requestHeadersUriMock;
    private WebClient.RequestBodySpec requestBodyMock;
    private WebClient.RequestBodyUriSpec requestBodyUriMock;
    private WebClient.ResponseSpec responseMock;
    private OpenAIProperties propertiesMock;

    @BeforeEach
    void setUp() {
        webClientMock = mock(WebClient.class);
        propertiesMock = Mockito.mock(OpenAIProperties.class);

        // Mocking the chain of WebClient
        requestHeadersUriMock = mock(WebClient.RequestHeadersUriSpec.class);
        requestBodyMock = mock(WebClient.RequestBodySpec.class);
        requestHeadersMock = mock(WebClient.RequestHeadersSpec.class);
        responseMock = mock(WebClient.ResponseSpec.class);

        when(webClientMock.post()).thenReturn(requestBodyUriMock);
        when(requestBodyMock.header(anyString(), anyString())).thenReturn(requestBodyMock);
        when(requestBodyMock.bodyValue(any())).thenReturn(requestHeadersMock);
        when(requestHeadersMock.retrieve()).thenReturn(responseMock);
        when(responseMock.bodyToMono(String.class)).thenReturn(Mono.just("response"));

        openAIService = new OpenAIService(WebClient.builder(), propertiesMock);
    }

    @Test
    void callOpenAI() {
        OpenAIProperties.ModelConfig modelConfig = new OpenAIProperties.ModelConfig();
        modelConfig.setName("testModel");
        modelConfig.setMaxTokens(100);
        modelConfig.setTemperature(0.5);

        when(propertiesMock.getApiKey()).thenReturn("testApiKey");
        when(propertiesMock.getBaseUrl()).thenReturn("https://api.openai.com");
        when(propertiesMock.getModels()).thenReturn(Collections.singletonList(modelConfig));

        openAIService.callOpenAI("testModel", "Test prompt").block();

        verify(webClientMock, times(1)).post();
        verify(requestBodyMock, times(1)).header("Authorization", "Bearer testApiKey");
        verify(requestBodyMock, times(1)).bodyValue(Mockito.<Map<String, Object>>any());
        verify(requestHeadersMock, times(1)).retrieve();
    }
}
