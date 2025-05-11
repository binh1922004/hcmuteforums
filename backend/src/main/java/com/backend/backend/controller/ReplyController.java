package com.backend.backend.controller;

import com.backend.backend.dto.ApiResponse;
import com.backend.backend.dto.request.ReplyPostRequest;
import com.backend.backend.dto.response.PageResponse;
import com.backend.backend.dto.response.ReplyResponse;
import com.backend.backend.service.LikeService;
import com.backend.backend.service.ReplyService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reply")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReplyController {
    ReplyService replyService;

    @PostMapping()
    public ApiResponse<ReplyResponse> replyTopic(@RequestBody ReplyPostRequest replyPostRequest) {
        return ApiResponse.<ReplyResponse>builder()
                .result(replyService.replyTopic(replyPostRequest))
                .build();
    }

    @PutMapping("/update")
    @PreAuthorize("@replyService.isOwner(#replyId)")
    public ApiResponse<ReplyResponse> updateReply(String replyId, String content){
        return ApiResponse.<ReplyResponse>builder()
                .result(replyService.updateReply(replyId, content))
                .build();
    }

    @DeleteMapping("/delete")
    @PreAuthorize("@replyService.isOwner(#replyId)")
    public ApiResponse<String> deleteReply(String replyId){
        return ApiResponse.<String>builder()
                .result(replyService.deleteReply(replyId))
                .build();
    }

    @GetMapping("/detail/{replyId}")
    public ApiResponse<ReplyResponse> detailReply(@PathVariable String replyId){
        return ApiResponse.<ReplyResponse>builder()
                .result(replyService.getDetailReply(replyId))
                .build();
    }

    @GetMapping("/{topicId}")
    public ApiResponse<PageResponse<ReplyResponse>> getAllRepliesByTopicId(
            @PathVariable("topicId") String topicId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction){
        return ApiResponse.<PageResponse<ReplyResponse>>builder()
                .result(replyService.getAllRepliesByTopicId(topicId, page, size, sortBy, direction))
                .build();
    }
    @GetMapping("/parent/{parentReplyId}")
    public ApiResponse<PageResponse<ReplyResponse>> getAllRepliesByParentReplyId(
            @PathVariable("parentReplyId") String parentReplyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction){
        return ApiResponse.<PageResponse<ReplyResponse>>builder()
                .result(replyService.getAllRepliesByParentReplyId(parentReplyId, page, size, sortBy, direction))
                .build();
    }
}
