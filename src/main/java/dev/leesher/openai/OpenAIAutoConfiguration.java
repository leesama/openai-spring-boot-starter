package dev.leesher.openai;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableConfigurationProperties(OpenAIProperties.class)
public class OpenAIAutoConfiguration {

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    @ConditionalOnProperty(name = "openai.enabled", havingValue = "true", matchIfMissing = true)
    public OpenAIService openAIService(WebClient.Builder webClientBuilder, OpenAIProperties properties) {
        return new OpenAIService(webClientBuilder, properties);
    }
}
