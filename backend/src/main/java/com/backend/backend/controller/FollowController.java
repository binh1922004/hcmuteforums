package com.backend.backend.controller;

import com.backend.backend.dto.ApiResponse;
import com.backend.backend.dto.request.FollowRequest;
import com.backend.backend.dto.response.*;
import com.backend.backend.mapper.FollowMapper;
import com.backend.backend.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/follow")
@RequiredArgsConstructor

public class FollowController {
    FollowService followService;
    @Autowired
    public FollowController(FollowService followService) {
        this.followService = followService;
    }
    @PostMapping
    public ApiResponse<FollowResponse> followUser(@RequestBody FollowRequest followRequest) {
        return ApiResponse.<FollowResponse>builder()
                .result(followService.followUser(followRequest))
                .build();
    }
    @DeleteMapping("/unfollow")
    public ApiResponse<FollowResponse> unfollowUser(@RequestParam String targetUsername) {
        return ApiResponse.<FollowResponse>builder()
                .result(followService.unfollowUser(targetUsername))
                .build();
    }
    @GetMapping("/followers")
    public ApiResponse<PageResponse<FollowerResponse>> getFollowers(
            @RequestParam String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction
    ) {
        return ApiResponse.<PageResponse<FollowerResponse>>builder()
                .result(followService.getFollowers(username, page, size, sortBy, direction))
                .build();
    }

    @GetMapping("/following")
    public ApiResponse<PageResponse<FollowingResponse>> getFollowing(
            @RequestParam String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction

    ) {
        return ApiResponse.<PageResponse<FollowingResponse>>builder()
                .result(followService.getFollowing(username, page, size, sortBy, direction))
                .build();
    }
    @GetMapping("/check")
    public ApiResponse<FollowStatusResponse> checkFollowStatus(
            @RequestParam("currentUsername") String currentUsername,
            @RequestParam("targetUsername") String targetUsername) {
        boolean isFollowing = followService.checkFollowStatus(currentUsername, targetUsername);
        return ApiResponse.<FollowStatusResponse>builder()
                .result(new FollowStatusResponse(isFollowing))
                .build();
    }
}
