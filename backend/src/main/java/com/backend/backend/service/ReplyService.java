package com.backend.backend.service;
import com.backend.backend.dto.NotificationDTO;
import com.backend.backend.dto.UserGeneral;
import com.backend.backend.dto.request.ReplyPostRequest;
import com.backend.backend.dto.request.TopicPostRequest;
import com.backend.backend.dto.response.PageResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    //service
    NotificationService notificationService;
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
        Reply savedReply = replyRepository.save(reply);
        ReplyResponse replyResponse = toReplyResponse(savedReply);

        // Lấy thông tin người cần nhận thông báo - ví dụ là chủ bài viết
        String targetUserId = topic.getUser().getId();

        // Chỉ gửi thông báo nếu người reply khác chủ bài viết
        if (!targetUserId.equals(user.getId())) {
            // Tạo và gửi một notification object thay vì text string
            NotificationDTO notification = notificationService.createReplyNotification(
                    targetUserId,
                    topic.getId(),
                    savedReply.getId(),
                    user.getFullName()
            );

            notificationService.sendStructuredNotificationToUser(targetUserId, notification);
        }

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

    public PageResponse<ReplyResponse> getAllRepliesByTopicId(
            String topicId,
            int page,
            int size,
            String sortBy,
            String direction){

        if (!topicRepository.existsById(topicId)) {
            throw new AppException(ErrorCode.TOPIC_NOTEXISTED);
        }
        Sort sort = direction.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Reply> replyPage = replyRepository.findByTopic_IdAndParentReplyIdIsNull(topicId, pageable);

        List<ReplyResponse> repliesResponse = new ArrayList<>();
        //mapping reply to replyresponse list
        for(Reply reply : replyPage.getContent()){
            repliesResponse.add(toReplyResponse(reply));
        }

        return PageResponse.<ReplyResponse>builder()
                .content(repliesResponse)
                .pageNumber(replyPage.getNumber())
                .pageSize(replyPage.getSize())
                .totalElements(replyPage.getTotalElements())
                .totalPages(replyPage.getTotalPages())
                .last(replyPage.isLast())
                .build();
    }
    public PageResponse<ReplyResponse> getAllRepliesByParentReplyId(
            String parentReplyId,
            int page,
            int size,
            String sortBy,
            String direction){
        if (!replyRepository.existsById(parentReplyId)) {
            throw new AppException(ErrorCode.REPLY_NOTEXISTED);
        }
        Sort sort = direction.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Reply> replyPage = replyRepository.findByParentReplyId(parentReplyId, pageable);

        List<ReplyResponse> repliesResponse = new ArrayList<>();
        //mapping reply to replyresponse list
        for(Reply reply : replyPage.getContent()){
            repliesResponse.add(toReplyResponse(reply));
        }

        return PageResponse.<ReplyResponse>builder()
                .content(repliesResponse)
                .pageNumber(replyPage.getNumber())
                .pageSize(replyPage.getSize())
                .totalElements(replyPage.getTotalElements())
                .totalPages(replyPage.getTotalPages())
                .last(replyPage.isLast())
                .build();
    }

    private ReplyResponse toReplyResponse(Reply reply){
        ReplyResponse replyResponse = replyMapper.toReplyResponse(reply);
        UserGeneral userGeneral = userMapper.toUserGeneral(reply.getUser());
        userGeneral.setAvt("http://10.0.2.2:8080/ute/" + reply.getUser().getProfile().getAvatarUrl());
        replyResponse.setUserGeneral(userGeneral);
        return replyResponse;
    }
}
