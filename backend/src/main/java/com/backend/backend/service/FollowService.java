package com.backend.backend.service;

import com.backend.backend.dto.request.FollowRequest;
import com.backend.backend.dto.response.FollowResponse;
import com.backend.backend.entity.Follow;
import com.backend.backend.entity.User;
import com.backend.backend.exception.AppException;
import com.backend.backend.exception.ErrorCode;
import com.backend.backend.mapper.FollowMapper;
import com.backend.backend.repository.FollowRepository;
import com.backend.backend.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class FollowService {
    FollowRepository followRepository;
    UserRepository userRepository;
    FollowMapper followMapper;
    public FollowResponse followUser(FollowRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User follower = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOTEXISTED));

        // Truy vấn theo username (mặc dù biến là "Id")
        User followed = userRepository.findByUsername(request.getFollowedUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOTEXISTED));

        // Kiểm tra không được follow chính mình
        if (follower.getUsername().equals(followed.getUsername())) {
            throw new AppException(ErrorCode.INVALID_OPERATION);
        }

        if (followRepository.existsByFollowerAndFollowed(follower, followed)) {
            throw new AppException(ErrorCode.DUPLICATE_RESOURCE);
        }

        Follow follow = Follow.builder()
                .follower(follower)
                .followed(followed)
                .status("approved")
                .createdAt(new Date())
                .build();

        return followMapper.toFollowResponse(followRepository.save(follow));
    }
    public FollowResponse unfollowUser(FollowRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User follower = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOTEXISTED));

        User followed = userRepository.findByUsername(request.getFollowedUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOTEXISTED));

        Follow follow = followRepository.findByFollowerAndFollowed(follower, followed)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));

        followRepository.delete(follow);

        return followMapper.toFollowResponse(follow);
    }
    public Page<FollowResponse> getFollowers(String username, int page, int size) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOTEXISTED));

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Follow> followers = followRepository.findAllByFollowed(user, pageable);
        return followers.map(followMapper::toFollowResponse);
    }

    public Page<FollowResponse> getFollowing(String username, int page, int size) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOTEXISTED));

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Follow> following = followRepository.findAllByFollower(user, pageable);
        return following.map(followMapper::toFollowResponse);
    }

}
