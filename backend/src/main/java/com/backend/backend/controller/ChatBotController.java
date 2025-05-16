package com.backend.backend.controller;


import com.backend.backend.dto.ApiResponse;
import com.backend.backend.service.GeminiService;
import com.google.api.client.http.javanet.NetHttpTransport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
@RestController
@RequestMapping("/api/chatbot")
public class ChatBotController {
    @Autowired
    private GeminiService geminiService;

    private static final String API_KEY = "AIzaSyCYTnBpEwZXaumdnL4rgoQai2B1GkA_kN8";
    private static final String API_URL1 =  "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key="+API_KEY;
    private static final String API_URL = "https://api-chatbot.hcmute.edu.vn/chat/ask/91bb9f16-d42c-476c-b49a-9467af853fc7";
    @PostMapping("/ask")
    public ApiResponse<String> askChatBot(@RequestParam String request) throws IOException, URISyntaxException, InterruptedException {
        String result = geminiService.generateContent(request).block();
        return ApiResponse.<String>builder()
                .result(result)
                .build();
    }
    public String askGeminiChatBot(@RequestParam String question) throws IOException, InterruptedException, URISyntaxException {
        HttpClient client = HttpClient.newHttpClient();

        // Tạo request
        java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                .uri(new URI(API_URL1))
                .header("Content-Type", "application/json")
                .header("X-GEMINI-APIKEY", API_KEY)
                .GET()
                .build();

        // Gửi request
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // In ra response
        System.out.println("Response Code: " + response.statusCode());
        System.out.println("Response Body: " + response.body());
        return response.body();
    }
    public String askChatBotService(String question) {
        RestTemplate restTemplate = new RestTemplate();

        // Tạo request body
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("question", question);

        // Cấu hình header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Tạo request entity
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        // Gửi request và nhận response
        ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.POST, requestEntity, String.class);

        // Trả về response body
        return response.getBody();
    }
}

