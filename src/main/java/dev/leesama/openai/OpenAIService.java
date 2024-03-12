package dev.leesama.openai;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

public class OpenAIService {

    private final WebClient webClient;
    private final OpenAIProperties properties;

    // 现在构造函数接受一个WebClient实例和OpenAIProperties实例
    public OpenAIService(WebClient webClient, OpenAIProperties properties) {
        this.webClient = webClient; // 直接使用提供的WebClient实例
        this.properties = properties;
    }

    public Mono<String> callOpenAI(String modelName, String prompt) {
        OpenAIProperties.ModelConfig modelConfig = properties.getModels().stream()
                .filter(m -> m.getName().equals(modelName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Model not configured: " + modelName));

        return this.webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/engines/{engine}/completions")
                        .build(modelName))
                .header("Authorization", "Bearer " + properties.getApiKey())
                .bodyValue(Map.of(
                        "prompt", prompt,
                        "max_tokens", modelConfig.getMaxTokens(),
                        "temperature", modelConfig.getTemperature()
                ))
                .retrieve()
                .bodyToMono(String.class);
    }
}
