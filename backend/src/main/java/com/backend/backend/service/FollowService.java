package com.backend.backend.service;

import com.backend.backend.dto.UserGeneral;
import com.backend.backend.dto.request.FollowRequest;
import com.backend.backend.dto.response.*;
import com.backend.backend.entity.*;
import com.backend.backend.exception.AppException;
import com.backend.backend.exception.ErrorCode;
import com.backend.backend.mapper.FollowMapper;
import com.backend.backend.repository.FollowRepository;
import com.backend.backend.repository.NotificationRepository;
import com.backend.backend.repository.TopicRepository;
import com.backend.backend.repository.UserRepository;
import com.backend.backend.utils.Constant;
import com.backend.backend.utils.NotificationContent;
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
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class FollowService {
    FollowRepository followRepository;
    UserRepository userRepository;
    FollowMapper followMapper;
    NotificationService notificationService;
    NotificationRepository notificationRepository;
    private final TopicRepository topicRepository;

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

        var savedFollow = followRepository.save(follow);
        Notification notification = Notification.builder()
                .actionId(follow.getId())
                .sendUser(follower)
                .recieveUser(followed)
                .createdAt(new Date())
                .content(NotificationContent.FOLLOW)
                .build();
        notificationService.sendStructuredNotificationToUserFollow(notification);
        return followMapper.toFollowResponse(savedFollow);
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
        System.out.println(follow.getId());
        Optional<Notification> notification = notificationRepository.findNotificationByActionId(follow.getId());
        if (notification.isPresent()){
            notificationRepository.delete(notification.get());
            followRepository.delete(follow);
        }

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
            String followerUserName = follow.getFollower().getUsername();
            boolean isCurrentUser = loginUserName != null &&
                    loginUserName.equals(followerUserName);

            UserGeneral userGeneral = UserGeneral.builder()
                    .fullName(follow.getFollower().getFullName())
                    .username(follow.getFollower().getUsername())
                    .avt(Constant.url+follow.getFollower().getProfile().getAvatarUrl())
                    .build();
            if (loginUserName != null){

                followResponses.add(FollowerResponse.builder()
                        .hasFollowed(followRepository.existsByFollower_UsernameAndFollowed_Username(loginUserName, follow.getFollower().getUsername()))
                        .currentMe(isCurrentUser)
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
            String followedUserName = follow.getFollowed().getUsername();
            boolean isCurrentUser = loginUserName != null &&
                    loginUserName.equals(followedUserName);
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
                        .currentMe(isCurrentUser)
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



    public PageResponse<FollowerResponse> getFollowersByUsername(String username, String targetUser, int page, int size
            ,String sortBy,String direction) {
        User user = userRepository.findByUsername(targetUser)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOTEXISTED));
        String loginUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        Sort sort = direction.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        List<FollowerResponse> followResponses = new ArrayList<>();
        Page<Follow> followerUserPage = followRepository.findAllByFollowedAndFollower_UsernameContaining(user, username, pageable);
        //mapping reply to follow response list
        for(var follow : followerUserPage.getContent()){
            String followerUserName = follow.getFollower().getUsername();
            boolean isCurrentUser = loginUserName != null &&
                    loginUserName.equals(followerUserName);

            UserGeneral userGeneral = UserGeneral.builder()
                    .fullName(follow.getFollower().getFullName())
                    .username(follow.getFollower().getUsername())
                    .avt(Constant.url+follow.getFollower().getProfile().getAvatarUrl())
                    .build();
            if (loginUserName != null){
                followResponses.add(FollowerResponse.builder()
                        .hasFollowed(followRepository.existsByFollower_UsernameAndFollowed_Username(loginUserName, follow.getFollower().getUsername()))
                        .currentMe(isCurrentUser)
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

    public PageResponse<FollowingResponse> getFollowingsByUsername(String username, String targetUser, int page, int size
            ,String sortBy,String direction) {
        User user = userRepository.findByUsername(targetUser)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOTEXISTED));

        String loginUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        Sort sort = direction.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        List<FollowingResponse> followingResponses = new ArrayList<>();
        Page<Follow> followingUserPage = followRepository.findAllByFollowerAndFollowed_UsernameContaining(user, username, pageable);
        for(var follow : followingUserPage.getContent()){
            String followedUserName = follow.getFollowed().getUsername();
            boolean isCurrentUser = loginUserName != null &&
                    loginUserName.equals(followedUserName);
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
                        .currentMe(isCurrentUser)
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

}
