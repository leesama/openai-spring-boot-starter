package dev.leesher.openai;

import dev.leesher.openai.OpenAIProperties;
import dev.leesher.openai.OpenAIService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class OpenAIServiceTest {

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    private OpenAIProperties properties;
    private OpenAIService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        properties = new OpenAIProperties();
        properties.setApiKey("test-api-key"); // 设置测试用的API Key
        properties.setBaseUrl("http://localhost"); // 设置测试用的Base URL

        // Mock WebClient
        when(webClientBuilder.baseUrl(any(String.class))).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.post()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header(any(), any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

        // 配置service
        service = new OpenAIService(webClientBuilder, properties);
    }

    @Test
    public void callOpenAIShouldReturnResult() {
        // 模拟成功的API响应
        String expectedResult = "test response";
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just(expectedResult));

        // 设置properties中的model配置
        OpenAIProperties.ModelConfig modelConfig = new OpenAIProperties.ModelConfig();
        modelConfig.setName("test-model");
        properties.setModels(List.of(modelConfig));

        // 执行service方法
        Mono<String> result = service.callOpenAI("test-model", "test prompt");

        // 使用StepVerifier验证响应
        StepVerifier.create(result)
                .expectNext(expectedResult)
                .verifyComplete();
    }

    // 这里可以添加更多测试用例，比如测试错误情况、模型未找到等情况
}
