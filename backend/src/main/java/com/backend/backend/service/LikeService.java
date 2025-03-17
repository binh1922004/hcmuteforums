package com.backend.backend.service;

import com.backend.backend.entity.Like;
import com.backend.backend.entity.Topic;
import com.backend.backend.entity.User;

import com.backend.backend.exception.AppException;
import com.backend.backend.exception.ErrorCode;
import com.backend.backend.repository.LikeRepository;
import com.backend.backend.repository.TopicRepository;
import com.backend.backend.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class LikeService {
    UserRepository userRepository;
    TopicRepository topicRepository;
    LikeRepository likeRepository;
    public Boolean likeTopic(String topicId){
        Topic topic = topicRepository.findById(topicId).orElseThrow(() -> new AppException(ErrorCode.TOPIC_NOTEXISTED));
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (likeRepository.existsLikeByTopic_IdAndUser_Username(topicId, username)){
            Like like = likeRepository.findLikeByTopic_IdAndUser_Username(topicId, username);
            likeRepository.delete(like);
        }
        else{
            User user = userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOTEXISTED));
            Like like = Like.builder()
                    .topic(topic)
                    .user(user)
                    .createdAt(new Date())
                    .build();
            likeRepository.save(like);
        }
        return true;
    }

}
