package com.backend.backend.service;
import com.backend.backend.dto.request.TopicPostRequest;
import com.backend.backend.dto.request.TopicUpdateRequest;
import com.backend.backend.dto.response.PageResponse;
import com.backend.backend.dto.response.TopicDetailResponse;
import com.backend.backend.entity.SubCategory;
import com.backend.backend.entity.Topic;
import com.backend.backend.entity.TopicImage;
import com.backend.backend.entity.User;
import com.backend.backend.dto.UserGeneral;
import com.backend.backend.exception.AppException;
import com.backend.backend.exception.ErrorCode;
import com.backend.backend.mapper.TopicMapper;
import com.backend.backend.mapper.UserMapper;
import com.backend.backend.repository.*;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class TopicService {
    //repo
    UserRepository userRepository;
    ProfileRepository profileRepository;
    SubCategoryRepository subCategoryRepository;
    TopicRepository topicRepository;
    LikeRepository likeRepository;
    ReplyRepository replyRepository;
    //mapper
    TopicMapper topicMapper;
    UserMapper userMapper;
    public PageResponse<TopicDetailResponse> getAllTopics(
            int page,
            int size,
            String sortBy,
            String direction) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (authentication != null && authentication.isAuthenticated()) ? authentication.getName() : null;

        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Topic> topicPage = topicRepository.findAll(pageable);

        List<TopicDetailResponse>  content= topicPage.getContent().stream()
                .map(this::toTopicDetailResponse)
                .collect(Collectors.toList());

        return PageResponse.<TopicDetailResponse>builder()
                .content(content)
                .pageSize(topicPage.getSize())
                .totalElements(topicPage.getTotalElements())
                .totalPages(topicPage.getTotalPages())
                .last(topicPage.isLast())
                .build();
    }

    public PageResponse<TopicDetailResponse> getTopicByUsername(
            String userName,
            int page,
            int size,
            String sortBy,
            String direction){

        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Topic> topicPage = topicRepository.findAllByUser_Username(userName, pageable);

        List<TopicDetailResponse>  content= topicPage.getContent().stream()
                .map(this::toTopicDetailResponse)
                .collect(Collectors.toList());

        return PageResponse.<TopicDetailResponse>builder()
                .content(content)
                .pageSize(topicPage.getSize())
                .totalElements(topicPage.getTotalElements())
                .totalPages(topicPage.getTotalPages())
                .last(topicPage.isLast())
                .build();
    }

    public TopicDetailResponse postTopic(TopicPostRequest topicPostRequest) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new AppException(ErrorCode.USER_NOTEXISTED);
        }

        Topic topic = topicMapper.toTopic(topicPostRequest);
        topic.setUser(user.get());
        topic.setCreatedAt(new Date());

        return toTopicDetailResponse(topicRepository.save(topic));
    }

    public TopicDetailResponse getTopicDetail(String topicId) {
        Topic topic = topicRepository.findById(topicId).orElseThrow(() ->
                new AppException(ErrorCode.TOPIC_NOTEXISTED));
       return toTopicDetailResponse(topic);
    }

    public String deleteTopic(String topicId) {
        topicRepository.deleteById(topicId);
        return topicId;
    }

    public void updateTopic(String topicId, TopicUpdateRequest topicUpdateRequest) {
        Topic topic = topicRepository.findById(topicId).orElseThrow(() -> new AppException(ErrorCode.TOPIC_NOTEXISTED));
        topicMapper.updateTopic(topic, topicUpdateRequest);
        Optional<SubCategory> subCategory = subCategoryRepository.findById(topicUpdateRequest.getSubCategoryId());
        topicRepository.save(topic);
    }

    public boolean isOwner(String topicId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(username + " " + topicRepository.existsTopicByIdAndUser_Username(topicId, username));
        return topicRepository.existsTopicByIdAndUser_Username(topicId, username);
    }

    private TopicDetailResponse toTopicDetailResponse(Topic topic) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (authentication != null && authentication.isAuthenticated()) ? authentication.getName() : null;

        String topicId = topic.getId();
        TopicDetailResponse topicDetailResponse = topicMapper.toTopicDetailResponse(topic);

        topicDetailResponse.setLikeCount(likeRepository.countByTopic_Id(topicId));
        topicDetailResponse.setReplyCount(replyRepository.countByTopic_Id(topicId));
        //is your topic
        if (username != null && username.equals(topic.getUser().getUsername())) {
            topicDetailResponse.setOwner(true);
        }
        //is like?
        if (username != null){
            if (likeRepository.existsLikeByTopic_IdAndUser_Username(topicId, username)) {
                topicDetailResponse.setLiked(true);
            }
            else{
                topicDetailResponse.setLiked(false);
            }
        }
        //get user
        UserGeneral userGeneral = userMapper.toUserGeneral(topic.getUser());
        userGeneral.setAvt(Constant.url + topic.getUser().getProfile().getAvatarUrl());
        topicDetailResponse.setUserGeneral(userGeneral);
        //map all url from topic image to imgUrls
        if (topic.getListImages() != null){
            topicDetailResponse.setImgUrls(topic.getListImages().stream().map(v -> {
                return Constant.url + v.getImageUrl();
            }).toList());
        }
        return topicDetailResponse;
    }
}
