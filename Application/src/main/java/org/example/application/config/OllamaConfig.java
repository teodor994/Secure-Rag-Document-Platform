package org.example.application.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;
import java.time.Duration;

@Configuration
public class OllamaConfig {
    @Value("${OLLAMA_KEY}")
    private String apiKey;

    @Bean
    public RestClient.Builder restClientBuilder() {
        HttpClient jdkHttpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        JdkClientHttpRequestFactory jdkRequestFactory = new JdkClientHttpRequestFactory(jdkHttpClient);
        jdkRequestFactory.setReadTimeout(Duration.ofSeconds(20));
        return RestClient.builder()
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .requestFactory(jdkRequestFactory);
    }
}