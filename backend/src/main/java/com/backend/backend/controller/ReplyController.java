package com.backend.backend.controller;

import com.backend.backend.dto.ApiResponse;
import com.backend.backend.dto.request.ReplyPostRequest;
import com.backend.backend.service.LikeService;
import com.backend.backend.service.ReplyService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reply")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReplyController {
    ReplyService replyService;

    @PostMapping()
    public ApiResponse replyTopic(@RequestBody ReplyPostRequest replyPostRequest) {
        replyService.replyTopic(replyPostRequest);
        return ApiResponse.builder().build();
    }


}
