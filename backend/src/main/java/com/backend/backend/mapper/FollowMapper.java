package com.backend.backend.mapper;

import com.backend.backend.dto.response.FollowResponse;
import com.backend.backend.entity.Follow;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FollowMapper {
    FollowResponse toFollowResponse(Follow follow);
}
