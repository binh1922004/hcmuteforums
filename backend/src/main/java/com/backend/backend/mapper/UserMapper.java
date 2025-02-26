package com.backend.backend.mapper;

import com.backend.backend.dto.request.UserCreationRequest;
import com.backend.backend.dto.request.UserUpdateRequest;
import com.backend.backend.dto.response.UserResponse;
import com.backend.backend.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest userCreationRequest);
    UserResponse toUserResponse(User user);
    void updateUser(@MappingTarget User user, UserUpdateRequest userUpdateRequest);
}
