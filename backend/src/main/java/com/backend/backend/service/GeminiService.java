package com.backend.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class GeminiService {
    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private String apiKey = "AIzaSyCYTnBpEwZXaumdnL4rgoQai2B1GkA_kN8";

    public GeminiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://generativelanguage.googleapis.com").build();
    }

    public Mono<String> generateContent(String text) {
        String requestBody = """
                {
                    "contents": [
                        {
                            "parts": [
                                {
                                    "text": "%s"
                                }
                            ]
                        }
                    ]
                }
                """.formatted(text);

        System.out.println("Request Body: " + requestBody);

        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1beta/models/gemini-2.0-flash:generateContent")
                        .queryParam("key", apiKey)
                        .build())
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    System.out.println("Response: " + response);
                    try {
                        JsonNode root = objectMapper.readTree(response);
                        return root.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText().trim();
                    } catch (Exception e) {
                        e.printStackTrace();
                        return "Error parsing response";
                    }
                });
    }
}

