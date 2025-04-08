package com.backend.backend.service;

import com.backend.backend.dto.request.ProfileUpdateRequest;
import com.backend.backend.dto.response.ProfileResponse;
import com.backend.backend.entity.Profile;
import com.backend.backend.entity.User;
import com.backend.backend.mapper.ProfileMapper;
import com.backend.backend.repository.ProfileRepository;
import com.backend.backend.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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

    public ProfileResponse getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String username = jwt.getSubject();
        Profile profile = profileRepository.findProfileByUser_Username(username).orElseThrow(()->
                new RuntimeException("User not found" + " " +username  ));

        return profileMapper.toProfileResponse(profile);
    }
    public Profile getAll(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Profile profile = profileRepository.findProfileByUser_Username(username).orElseThrow(()->
                new RuntimeException("User not found" + " " +username  ));
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
}
