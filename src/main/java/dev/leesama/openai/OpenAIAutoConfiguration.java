package dev.leesama.openai;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableConfigurationProperties(OpenAIProperties.class)
public class OpenAIAutoConfiguration {

    @Bean
    public WebClient webClient(WebClient.Builder webClientBuilder, OpenAIProperties properties) {
        return webClientBuilder.baseUrl(properties.getBaseUrl()).build();
    }
    @Bean
    @ConditionalOnProperty(name = "openai.enabled", havingValue = "true", matchIfMissing = true)
    public OpenAIService openAIService(WebClient webClient, OpenAIProperties properties) {
        return new OpenAIService(webClient, properties);
    }
}
