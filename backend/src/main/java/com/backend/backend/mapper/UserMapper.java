package com.backend.backend.mapper;

import com.backend.backend.dto.request.UserCreationRequest;
import com.backend.backend.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest userCreationRequest);
}
