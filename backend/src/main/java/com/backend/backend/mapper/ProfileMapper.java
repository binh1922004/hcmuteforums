package com.backend.backend.mapper;

import com.backend.backend.dto.request.ProfileUpdateRequest;
import com.backend.backend.dto.response.ProfileResponse;
import com.backend.backend.entity.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    ProfileResponse toProfileResponse(Profile profile);
    void updateProfile(@MappingTarget Profile profile, ProfileUpdateRequest profileUpdateRequest);
}
