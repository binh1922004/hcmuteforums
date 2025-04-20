package com.backend.backend.mapper;

import com.backend.backend.dto.request.TopicPostRequest;
import com.backend.backend.dto.request.TopicUpdateRequest;
import com.backend.backend.dto.response.ReplyResponse;
import com.backend.backend.dto.response.TopicDetailResponse;
import com.backend.backend.entity.Reply;
import com.backend.backend.entity.Topic;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ReplyMapper {
    ReplyResponse toReplyResponse(Reply reply);
}
