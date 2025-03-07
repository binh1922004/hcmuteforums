package com.backend.backend.controller;

import com.backend.backend.dto.ApiResponse;
import com.backend.backend.dto.request.TopicPostRequest;
import com.backend.backend.dto.request.UserCreationRequest;
import com.backend.backend.dto.request.UserUpdateRequest;
import com.backend.backend.dto.response.UserResponse;
import com.backend.backend.service.TopicService;
import com.backend.backend.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/topics")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TopicController {
    TopicService topicService;

    @PostMapping("/post")
    public ApiResponse<Boolean> postTopic(@RequestBody TopicPostRequest topicPostRequest) {
        System.out.println("GET POST TOPIC");
        return ApiResponse.<Boolean>builder()
                .result(topicService.postTopic(topicPostRequest))
                .build();
    }

}
