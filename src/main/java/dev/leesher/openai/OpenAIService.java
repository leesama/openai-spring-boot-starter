package dev.leesher.openai;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

public class OpenAIService {

    private final WebClient webClient;
    private final OpenAIProperties properties;

    public OpenAIService(WebClient.Builder webClientBuilder, OpenAIProperties properties) {
        this.properties = properties;
        this.webClient = webClientBuilder.baseUrl(properties.getBaseUrl()).build();
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
