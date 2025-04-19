package com.backend.backend.service;
import com.backend.backend.dto.request.TopicPostRequest;
import com.backend.backend.dto.request.TopicUpdateRequest;
import com.backend.backend.dto.response.TopicDetailResponse;
import com.backend.backend.entity.SubCategory;
import com.backend.backend.entity.Topic;
import com.backend.backend.entity.User;
import com.backend.backend.dto.UserGeneral;
import com.backend.backend.exception.AppException;
import com.backend.backend.exception.ErrorCode;
import com.backend.backend.mapper.TopicMapper;
import com.backend.backend.mapper.UserMapper;
import com.backend.backend.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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
public class TopicService {
    //repo
    UserRepository userRepository;
    SubCategoryRepository subCategoryRepository;
    TopicRepository topicRepository;
    LikeRepository likeRepository;
    ReplyRepository replyRepository;
    //mapper
    TopicMapper topicMapper;
    UserMapper userMapper;
    public List<TopicDetailResponse> getAllTopics(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (authentication != null && authentication.isAuthenticated()) ? authentication.getName() : null;

        List<Topic> listTopic = topicRepository.findAll();
        List<TopicDetailResponse> topicDetailResponseList = new ArrayList<>();
        //map all topic to topic response
        listTopic.forEach(topic -> {
            TopicDetailResponse topicDetailResponse = toTopicDetailResponse(topic, username);
            topicDetailResponseList.add(topicDetailResponse);
        });

        return topicDetailResponseList;
    }
    public TopicDetailResponse postTopic(TopicPostRequest topicPostRequest) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return null;
        }

        Topic topic = topicMapper.toTopic(topicPostRequest);
        topic.setUser(user.get());
        topic.setCreatedAt(new Date());

        return toTopicDetailResponse(topicRepository.save(topic), username);
    }

    public TopicDetailResponse getTopicDetail(String topicId) {
        Topic topic = topicRepository.findById(topicId).orElseThrow(() ->
                new AppException(ErrorCode.TOPIC_NOTEXISTED));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (authentication != null && authentication.isAuthenticated()) ? authentication.getName() : null;
        return toTopicDetailResponse(topic, username);
    }

    public void deleteTopic(String topicId) {
        topicRepository.deleteById(topicId);
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

    private TopicDetailResponse toTopicDetailResponse(Topic topic, String username) {
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
        }
        //get user
        UserGeneral userGeneral = userMapper.toUserGeneral(topic.getUser());
        topicDetailResponse.setUserGeneral(userGeneral);
        return topicDetailResponse;
    }
}
