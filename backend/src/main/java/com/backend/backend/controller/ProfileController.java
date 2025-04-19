package com.backend.backend.controller;

import com.backend.backend.dto.ApiResponse;
import com.backend.backend.dto.request.ProfileUpdateRequest;
import com.backend.backend.dto.response.ProfileResponse;
import com.backend.backend.entity.Profile;
import com.backend.backend.service.AvatarImageService;
import com.backend.backend.service.CoverImageService;
import com.backend.backend.service.ProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProfileController {
    ProfileService profileService;
    AvatarImageService avatarImageService;
    CoverImageService coverImageService;


    @GetMapping("/myProfile")
    public ApiResponse<ProfileResponse> myProfile() {
        return ApiResponse.<ProfileResponse>builder()
                .result(profileService.getProfile())
                .message("Success")
                .build();
    }

    @PutMapping("/updateProfile")
    public ApiResponse<ProfileResponse> updateProfile(@RequestBody ProfileUpdateRequest profileUpdateRequest)
    {
        return ApiResponse.<ProfileResponse>builder()
                .result(profileService.updateProfile(profileUpdateRequest))
                .message("Success")
                .build();
    }
    @GetMapping("/getAll")
    public ApiResponse<Profile> getAll(){
        return ApiResponse.<Profile>builder()
                .result(profileService.getAll())
                .build();
    }
    @PostMapping("/upload-avatar")
    public ResponseEntity<?> uploadAvatar(@RequestParam MultipartFile file) {

        avatarImageService.uploadAvatar(file);
        return ResponseEntity.ok("Avatar uploaded");
    }
    @PostMapping("/upload-cover")
    public ResponseEntity<?> uploadCover(@RequestParam MultipartFile file) {    

        coverImageService.uploadCoverImage(file);
        return ResponseEntity.ok("Cover uploaded");
    }
}
