package com.backend.backend.controller;

import com.backend.backend.dto.ApiResponse;
import com.backend.backend.dto.request.TopicPostRequest;
import com.backend.backend.dto.request.TopicUpdateRequest;
import com.backend.backend.dto.request.UserCreationRequest;
import com.backend.backend.dto.request.UserUpdateRequest;
import com.backend.backend.dto.response.PageResponse;
import com.backend.backend.dto.response.TopicDetailResponse;
import com.backend.backend.dto.response.UserResponse;
import com.backend.backend.entity.Topic;
import com.backend.backend.service.TopicService;
import com.backend.backend.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/topics")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TopicController {
    TopicService topicService;

    @PostMapping("/post")
    public ApiResponse<TopicDetailResponse> postTopic(@RequestBody TopicPostRequest topicPostRequest) {
        System.out.println("GET POST TOPIC");
        return ApiResponse.<TopicDetailResponse>builder()
                .result(topicService.postTopic(topicPostRequest))
                .build();
    }

//    @GetMapping("/list/{id}")
//    public ApiResponse<List<Topic>> getAllTopicsBySubCategory(@PathVariable String id) {
//        return ApiResponse.<List<Topic>>builder()
//                .result(topicService.getAllTopicsBySubCategory(id))
//                .build();
//    }

    @GetMapping("/detail/{id}")
    public ApiResponse<TopicDetailResponse> topicDetail(@PathVariable String id) {
        return ApiResponse.<TopicDetailResponse>builder()
                .result(topicService.getTopicDetail(id))
                .build();
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("@topicService.isOwner(#id)")
    public ApiResponse<String> deleteTopic(@PathVariable String id) {
        topicService.deleteTopic(id);
        return ApiResponse.<String>builder()
                .result("Đã xoá bài viết")
                .build();
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("@topicService.isOwner(#id)")
    public ApiResponse<String> updateTopic(@PathVariable String id, @RequestBody TopicUpdateRequest topicUpdateRequest) {
        topicService.updateTopic(id, topicUpdateRequest);
        return ApiResponse.<String>builder()
                .result("Đã cập nhật thông tin bài viết")
                .build();
    }
    @GetMapping
    public ApiResponse<PageResponse<TopicDetailResponse>> getAllTopics(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction
    ){
        return ApiResponse.<PageResponse<TopicDetailResponse>>builder()
                .result(topicService.getAllTopics(page, size, sortBy, direction))
                .build();
    }
}
