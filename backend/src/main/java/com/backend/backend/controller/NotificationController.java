package com.backend.backend.controller;

import com.backend.backend.dto.ApiResponse;
import com.backend.backend.dto.NotificationDTO;

import com.backend.backend.dto.response.PageResponse;
import com.backend.backend.service.NotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationController {
    NotificationService notificationService;



//    @DeleteMapping("/delete")
//    @PreAuthorize("@replyService.isOwner(#replyId)")
//    public ApiResponse deleteReply(String replyId){
//        notificationService.deleteReply(replyId);
//        return ApiResponse.builder().build();
//    }


    @GetMapping()
    public ApiResponse<PageResponse<NotificationDTO>> getAllRepliesByParentReplyId(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction){
        return ApiResponse.<PageResponse<NotificationDTO>>builder()
                .result(notificationService.getNotifications(page, size, sortBy, direction))
                .build();
    }
}
