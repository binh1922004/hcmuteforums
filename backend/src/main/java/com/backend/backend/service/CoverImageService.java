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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CoverImageService {
    UserRepository userRepository;
    ProfileRepository profileRepository;
    @NonFinal
    @Value("${upload.cover-dir}")
    private String uploadCoverImage;

    public boolean uploadCoverImage(MultipartFile image) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<Profile> optionalProfile = profileRepository.findProfileByUser_Username(username);

        User user = userRepository.findByUsername(username).orElseThrow(()->
                new AppException(ErrorCode.USER_NOTEXISTED));

        try {
            String filename = user.getId() + "-" + UUID.randomUUID().toString() + ".jpg";
            Path path = Paths.get(uploadCoverImage, filename);
            image.transferTo(new File(path.toAbsolutePath().toString()));
            String imageUrl = uploadCoverImage + filename;

            Profile profile = optionalProfile
                    .orElse(Profile.builder().user(user).build());

            profile.setCoverUrl(imageUrl);
            profileRepository.save(profile);

            return true;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void clearPresentCoverImage(Optional<Profile> optionalProfile) {
        optionalProfile.ifPresent( profile -> {
            String oldCoverImage = profile.getCoverUrl();
            if (oldCoverImage != null) {
                try {
                    Path oldPath = Paths.get(oldCoverImage);
                    Files.deleteIfExists(oldPath);
                }catch (IOException e) {
                    System.err.println("Không thể xóa ảnh cũ: " + e.getMessage());
                }
            }
        }
        );
    }
}
