package com.backend.backend.service;

import com.backend.backend.entity.Profile;
import com.backend.backend.entity.User;
import com.backend.backend.exception.AppException;
import com.backend.backend.exception.ErrorCode;
import com.backend.backend.repository.ProfileRepository;
import com.backend.backend.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AvatarImageService {
    //repo
    UserRepository userRepository;
    ProfileRepository profileRepository;
    @NonFinal
    @Value("${upload.avatar-dir}")
    private String uploadAvatar;

    public boolean uploadAvatar(MultipartFile image) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<Profile> optionalProfile = profileRepository.findProfileByUser_Username(username);

        User user = userRepository.findByUsername(username).orElseThrow(()->
                new AppException(ErrorCode.USER_NOTEXISTED));
        try {
            String filename = user.getId() + "-" + UUID.randomUUID().toString() + ".jpg";
            Path path = Paths.get(uploadAvatar, filename);
            image.transferTo(new File(path.toAbsolutePath().toString()));
            String imageUrl = uploadAvatar + filename;

            Profile profile = optionalProfile
                    .orElse(Profile.builder().user(user).build());

            profile.setAvatarUrl(imageUrl);
            profileRepository.save(profile);
            return true;

        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
