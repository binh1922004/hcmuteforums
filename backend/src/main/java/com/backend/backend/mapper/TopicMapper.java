package com.backend.backend.mapper;

import com.backend.backend.dto.request.TopicPostRequest;
import com.backend.backend.dto.request.UserCreationRequest;
import com.backend.backend.dto.request.UserUpdateRequest;
import com.backend.backend.dto.response.TopicDetailResponse;
import com.backend.backend.dto.response.UserResponse;
import com.backend.backend.entity.Topic;
import com.backend.backend.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TopicMapper {
    Topic toTopic(TopicPostRequest topicPostRequest);
    TopicDetailResponse toTopicDetailResponse(Topic topic);
}
