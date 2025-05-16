package com.backend.backend.controller;


import com.backend.backend.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/chatbot")
public class ChatBotController {


    private static final String API_URL = "https://api-chatbot.hcmute.edu.vn/chat/ask/91bb9f16-d42c-476c-b49a-9467af853fc7";
    @PostMapping("/ask")
    public ApiResponse<String> askChatBot(@RequestParam String request) {
        return ApiResponse.<String>builder()
                .result(askChatBotService(request))
                .build();
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

