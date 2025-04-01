package com.backend.backend.controller;

import com.backend.backend.dto.ApiResponse;
import com.backend.backend.dto.request.TopicPostRequest;
import com.backend.backend.dto.request.TopicUpdateRequest;
import com.backend.backend.dto.response.TopicDetailResponse;
import com.backend.backend.entity.Topic;
import com.backend.backend.service.TopicImageService;
import com.backend.backend.service.TopicService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/topic-images")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TopicImageController {
    TopicImageService topicImageService;

    @PostMapping("/{topicId}/upload")
    public ApiResponse<Boolean> uploadImage(@PathVariable String topicId, @RequestParam("images") List<MultipartFile> images) {
        return ApiResponse.<Boolean>builder()
                .result(topicImageService.uploadImages(topicId, images))
                .build();
    }

}
