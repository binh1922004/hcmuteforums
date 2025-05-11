package com.backend.backend.service;

import com.backend.backend.dto.request.ProfileUpdateRequest;
import com.backend.backend.dto.response.ProfileResponse;
import com.backend.backend.entity.Profile;
import com.backend.backend.entity.User;
import com.backend.backend.exception.AppException;
import com.backend.backend.exception.ErrorCode;
import com.backend.backend.mapper.ProfileMapper;
import com.backend.backend.repository.ProfileRepository;
import com.backend.backend.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ProfileService {
    ProfileRepository profileRepository;
    UserRepository userRepository;
    ProfileMapper profileMapper;

    @Value("${upload.avatar-dir}")
    @NonFinal
    String avatarDir;
    @Value("${upload.cover-dir}")
    @NonFinal
    String coverDir;

    public ProfileResponse getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String username = jwt.getSubject();
        Profile profile = profileRepository.findProfileByUser_Username(username).orElseThrow(()->
                new AppException(ErrorCode.USER_NOTEXISTED));

        return profileMapper.toProfileResponse(profile);
    }
    public ProfileResponse getProfilePerson(String username) {
        Profile profile = profileRepository.findProfileByUser_Username(username).orElseThrow(()->
                new AppException(ErrorCode.USER_NOTEXISTED));

        return profileMapper.toProfileResponse(profile);
    }
    public Profile getAll(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Profile profile = profileRepository.findProfileByUser_Username(username).orElseThrow(()->
                new AppException(ErrorCode.USER_NOTEXISTED));
        return profile;
    }

    public ProfileResponse updateProfile(ProfileUpdateRequest profileUpdateRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Profile profile = profileRepository.findProfileByUser_Username(username).orElseThrow(() ->
                new RuntimeException("User not found"));
        profileMapper.updateProfile(profile, profileUpdateRequest);

        return profileMapper.toProfileResponse(profileRepository.save(profile));
    }

    public Profile createProfile(User user){
        Profile profile = Profile.builder()
                .user(user)
                .coverUrl(coverDir + "img_cover.png")
                .avatarUrl(avatarDir + "img_avatar.png")
                .build();

        return profileRepository.save(profile);
    }
}
