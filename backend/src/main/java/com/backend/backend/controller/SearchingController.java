package com.backend.backend.controller;

import com.backend.backend.dto.ApiResponse;
import com.backend.backend.dto.response.FollowerResponse;
import com.backend.backend.dto.response.FollowingResponse;
import com.backend.backend.dto.response.PageResponse;
import com.backend.backend.dto.response.TopicDetailResponse;
import com.backend.backend.service.FollowService;
import com.backend.backend.service.LikeService;
import com.backend.backend.service.TopicService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SearchingController {
    TopicService topicService;
    FollowService followService;
    @GetMapping()
    public ApiResponse<PageResponse<TopicDetailResponse>> searchTopic(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction
    ){
        return ApiResponse.<PageResponse<TopicDetailResponse>>builder()
                .result(topicService.searchTopic(keyword, page, size, sortBy, direction))
                .build();
    }

    @GetMapping("/following")
    public ApiResponse<PageResponse<FollowingResponse>> searchFollowerResponseByUsername(
            @RequestParam String username,
            @RequestParam String targetUsername,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction
    ){
        return ApiResponse.<PageResponse<FollowingResponse>>builder()
                .result(followService.getFollowingsByUsername(username, targetUsername, page, size, sortBy, direction))
                .build();
    }

    @GetMapping("/follower")
    public ApiResponse<PageResponse<FollowerResponse>> searchFollowingResponseByUsername(
            @RequestParam String username,
            @RequestParam String targetUsername,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction
    ){
        return ApiResponse.<PageResponse<FollowerResponse>>builder()
                .result(followService.getFollowersByUsername(username, targetUsername, page, size, sortBy, direction))
                .build();
    }
}
