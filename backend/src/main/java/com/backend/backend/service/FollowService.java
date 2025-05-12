package com.backend.backend.service;

import com.backend.backend.dto.UserGeneral;
import com.backend.backend.dto.request.FollowRequest;
import com.backend.backend.dto.response.*;
import com.backend.backend.entity.Follow;
import com.backend.backend.entity.Reply;
import com.backend.backend.entity.User;
import com.backend.backend.exception.AppException;
import com.backend.backend.exception.ErrorCode;
import com.backend.backend.mapper.FollowMapper;
import com.backend.backend.repository.FollowRepository;
import com.backend.backend.repository.UserRepository;
import com.backend.backend.utils.Constant;
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

import java.util.ArrayList;
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

    public FollowResponse unfollowUser(String targetUsername) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User follower = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOTEXISTED));

        User followed = userRepository.findByUsername(targetUsername)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOTEXISTED));

        Follow follow = followRepository.findByFollowerAndFollowed(follower, followed)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));

        followRepository.delete(follow);

        return followMapper.toFollowResponse(follow);
    }
    public PageResponse<FollowerResponse> getFollowers(String username, int page, int size
                        ,String sortBy,String direction) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOTEXISTED));
        String loginUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        Sort sort = direction.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        List<FollowerResponse> followResponses = new ArrayList<>();
        Page<Follow> followerUserPage = followRepository.findAllByFollowed(user, pageable);
        //mapping reply to follow response list
        for(var follow : followerUserPage.getContent()){
            UserGeneral userGeneral = UserGeneral.builder()
                    .fullName(follow.getFollower().getFullName())
                    .username(follow.getFollower().getUsername())
                    .avt(Constant.url+follow.getFollower().getProfile().getAvatarUrl())
                    .build();
            if (loginUserName != null){

                followResponses.add(FollowerResponse.builder()
                        .hasFollowed(followRepository.existsByFollower_UsernameAndFollowed_Username(loginUserName, follow.getFollower().getUsername()))
                        .followId(follow.getId())
                        .userGeneral(userGeneral)
                        .build());
            }
            else{

                followResponses.add(FollowerResponse.builder()
                        .hasFollowed(false)
                        .followId(follow.getId())
                        .userGeneral(userGeneral)
                        .build());
            }
        }

        return PageResponse.<FollowerResponse>builder()
                .content(followResponses)
                .pageNumber(followerUserPage.getNumber())
                .pageSize(followerUserPage.getSize())
                .totalElements(followerUserPage.getTotalElements())
                .totalPages(followerUserPage.getTotalPages())
                .last(followerUserPage.isLast())
                .build();
    }

    public PageResponse<FollowingResponse> getFollowing(String username, int page, int size,String sortBy,String direction) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOTEXISTED));

        String loginUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        Sort sort = direction.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        List<FollowingResponse> followingResponses = new ArrayList<>();
        Page<Follow> followingUserPage = followRepository.findAllByFollower(user, pageable);
        for(var follow : followingUserPage.getContent()){
            UserGeneral userGeneral = UserGeneral.builder()
                    .fullName(follow.getFollowed().getFullName())
                    .username(follow.getFollowed().getUsername())
                    .avt(Constant.url+follow.getFollowed().getProfile().getAvatarUrl())
                    .build();

            if (loginUserName == null){
                followingResponses.add(FollowingResponse.builder()
                        .followId(follow.getId())
                        .hasFollowed(false)
                        .userGeneral(userGeneral)
                        .build());
            }
            else{
                followingResponses.add(FollowingResponse.builder()
                        .followId(follow.getId())
                        .hasFollowed(loginUserName.equals(username) ||
                                followRepository.existsByFollower_UsernameAndFollowed_Username(loginUserName, follow.getFollowed().getUsername()))
                        .userGeneral(userGeneral)
                        .build());
            }
        }
        return PageResponse.<FollowingResponse>builder()
                .content(followingResponses)
                .pageNumber(followingUserPage.getNumber())
                .pageSize(followingUserPage.getSize())
                .totalElements(followingUserPage.getTotalElements())
                .totalPages(followingUserPage.getTotalPages())
                .last(followingUserPage.isLast())
                .build();
    }
    public boolean checkFollowStatus(String currentUsername, String targetUsername) {
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOTEXISTED));
        User targetUser = userRepository.findByUsername(targetUsername)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOTEXISTED));

        if (currentUser.getUsername().equals(targetUser.getUsername())) {
            return false; // Không follow chính mình
        }

        return followRepository.existsByFollowerAndFollowed(currentUser, targetUser);
    }

}
