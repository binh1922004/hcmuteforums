package com.backend.backend.service;
import com.backend.backend.dto.UserGeneral;
import com.backend.backend.dto.request.ReplyPostRequest;
import com.backend.backend.dto.request.TopicPostRequest;
import com.backend.backend.dto.response.ReplyResponse;
import com.backend.backend.dto.response.TopicDetailResponse;
import com.backend.backend.entity.Reply;
import com.backend.backend.entity.SubCategory;
import com.backend.backend.entity.Topic;
import com.backend.backend.entity.User;
import com.backend.backend.exception.AppException;
import com.backend.backend.exception.ErrorCode;
import com.backend.backend.mapper.ReplyMapper;
import com.backend.backend.mapper.TopicMapper;
import com.backend.backend.mapper.UserMapper;
import com.backend.backend.repository.ReplyRepository;
import com.backend.backend.repository.SubCategoryRepository;
import com.backend.backend.repository.TopicRepository;
import com.backend.backend.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ReplyService {
    ReplyRepository replyRepository;
    TopicRepository topicRepository;
    UserRepository userRepository;
    //mapper
    UserMapper userMapper;
    ReplyMapper replyMapper;
    public ReplyResponse replyTopic(ReplyPostRequest replyPostRequest) {
        Topic topic = topicRepository.findById(replyPostRequest.getTopicId()).orElseThrow(() -> new AppException(ErrorCode.TOPIC_NOTEXISTED));
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOTEXISTED));

        Reply reply = Reply.builder()
                .content(replyPostRequest.getContent())
                .parentReplyId(replyPostRequest.getParentReplyId())
                .user(user)
                .topic(topic)
                .createdAt(new Date())
                .build();
        ReplyResponse replyResponse = replyMapper.toReplyResponse(replyRepository.save(reply));
        replyResponse.setUserGeneral(
                userMapper.toUserGeneral(user)
        );
        return replyResponse;
    }

    public void updateReply(String replyId, String content){
        Reply reply = replyRepository.findRepliesById(replyId);
        reply.setContent(content);
        replyRepository.save(reply);
    }
    @Transactional
    public void deleteReply(String replyId){
        replyRepository.deleteRepliesByParentReplyId(replyId);
        replyRepository.deleteById(replyId);
    }

    public boolean isOwner(String replyId){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return replyRepository.existsRepliesByIdAndUser_Username(replyId, username);
    }

    public List<ReplyResponse> getAllRepliesByTopicId(String topicId){
        List<Reply> replies = replyRepository.getAllByTopic_Id(topicId);
        List<ReplyResponse> repliesResponse = new ArrayList<>();
        for(Reply reply : replies){
            repliesResponse.add(replyMapper.toReplyResponse(reply));
            repliesResponse.getLast().setUserGeneral(userMapper.toUserGeneral(reply.getUser()));
        }
        return repliesResponse;
    }
}
