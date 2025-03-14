package com.backend.backend.service;
import com.backend.backend.dto.request.TopicPostRequest;
import com.backend.backend.dto.request.TopicUpdateRequest;
import com.backend.backend.dto.response.TopicDetailResponse;
import com.backend.backend.entity.SubCategory;
import com.backend.backend.entity.Topic;
import com.backend.backend.entity.User;
import com.backend.backend.exception.AppException;
import com.backend.backend.exception.ErrorCode;
import com.backend.backend.mapper.TopicMapper;
import com.backend.backend.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class TopicService {
    //repo
    UserRepository userRepository;
    SubCategoryRepository subCategoryRepository;
    TopicRepository topicRepository;
    LikeRepository likeRepository;
    ReplyRepository replyRepository;
    //mapper
    TopicMapper topicMapper;
    public boolean postTopic(TopicPostRequest topicPostRequest) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepository.findByUsername(username);
        Optional<SubCategory> subCategory = subCategoryRepository.findById(topicPostRequest.getSubCategoryId());
        if (user.isEmpty() || subCategory.isEmpty()) {
            return false;
        }

        Topic topic = topicMapper.toTopic(topicPostRequest);
        topic.setUser(user.get());
        topic.setSubCategory(subCategory.get());
        topic.setCreatedAt(new Date());

        topicRepository.save(topic);
        return true;
    }

    public List<Topic> getAllTopicsBySubCategory(String subCategoryId) {
        return topicRepository.getTopicsBySubCategory_Id(subCategoryId);
    }

    public TopicDetailResponse getTopicDetail(String topicId) {
        Topic topic = topicRepository.findById(topicId).orElseThrow(() ->
                new AppException(ErrorCode.TOPIC_NOTEXISTED));
        TopicDetailResponse topicDetailResponse = topicMapper.toTopicDetailResponse(topic);
        System.out.println(topicDetailResponse.getContent() + " " + topic.getContent());
        topicDetailResponse.setFullName(topic.getUser().getFullName());
        topicDetailResponse.setLikeCount(likeRepository.countByTopic_Id(topicId));
        topicDetailResponse.setReplyCount(replyRepository.countByTopic_Id(topicId));
        //is your topic
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (authentication != null && authentication.isAuthenticated()) ? authentication.getName() : null;
        if (username != null && username.equals(topic.getUser().getUsername())) {
            topicDetailResponse.setOwner(true);
        }
        //is like?
        if (username != null){
            if (likeRepository.existsLikeByTopic_IdAndUser_Username(topicId, username)) {
                topicDetailResponse.setOwner(true);
            }
        }
        return topicDetailResponse;
    }

    public void deleteTopic(String topicId) {
        topicRepository.deleteById(topicId);
    }

    public void updateTopic(String topicId, TopicUpdateRequest topicUpdateRequest) {
        Topic topic = topicRepository.findById(topicId).orElseThrow(() -> new AppException(ErrorCode.TOPIC_NOTEXISTED));
        topicMapper.updateTopic(topic, topicUpdateRequest);
        Optional<SubCategory> subCategory = subCategoryRepository.findById(topicUpdateRequest.getSubCategoryId());
        subCategory.ifPresent(topic::setSubCategory);

        topicRepository.save(topic);
    }

    public boolean isOwner(String topicId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(username + " " + topicRepository.existsTopicByIdAndUser_Username(topicId, username));
        return topicRepository.existsTopicByIdAndUser_Username(topicId, username);
    }
}
