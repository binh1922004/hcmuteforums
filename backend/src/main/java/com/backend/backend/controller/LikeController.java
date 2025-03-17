package com.backend.backend.controller;

import com.backend.backend.dto.ApiResponse;
import com.backend.backend.dto.request.UserCreationRequest;
import com.backend.backend.dto.request.UserUpdateRequest;
import com.backend.backend.dto.response.UserResponse;
import com.backend.backend.service.LikeService;
import com.backend.backend.service.UserService;
import io.lettuce.core.dynamic.annotation.Param;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/like")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LikeController {
    LikeService likeService;

    @PostMapping
    public ApiResponse<Boolean> likeTopic(@RequestParam String topicId){
        return ApiResponse.<Boolean>builder()
                .result(likeService.likeTopic(topicId))
                .build();
    }


}
