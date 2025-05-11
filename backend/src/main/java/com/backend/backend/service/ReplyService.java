package com.backend.backend.service;
import com.backend.backend.dto.UserGeneral;
import com.backend.backend.dto.request.ReplyPostRequest;
import com.backend.backend.dto.response.PageResponse;
import com.backend.backend.dto.response.ReplyResponse;
import com.backend.backend.entity.*;
import com.backend.backend.exception.AppException;
import com.backend.backend.exception.ErrorCode;
import com.backend.backend.mapper.ReplyMapper;
import com.backend.backend.mapper.UserMapper;
import com.backend.backend.repository.ReplyRepository;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

        //find info about user send and user received notification
        String sendUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User sendUser = userRepository.findByUsername(sendUsername).orElseThrow(() -> new AppException(ErrorCode.USER_NOTEXISTED));
        String receivedUsername = topic.getUser().getUsername();
        User receivedUser = userRepository.findByUsername(receivedUsername).orElseThrow(() -> new AppException(ErrorCode.USER_NOTEXISTED));


        //create new reply
        Reply reply = Reply.builder()
                .content(replyPostRequest.getContent())
                .parentReplyId(replyPostRequest.getParentReplyId())
                .targetUserName(replyPostRequest.getTargetUserName())
                .user(sendUser)
                .topic(topic)
                .createdAt(new Date())
                .build();
        Reply savedReply = replyRepository.save(reply);
        ReplyResponse replyResponse = toReplyResponse(savedReply);


        // Chỉ gửi thông báo nếu người reply khác chủ bài viết
        if (!receivedUser.getId().equals(sendUser.getId())) {
            // Tạo và gửi một notification object thay vì text string
            Notification notification = notificationService.createNotification(
                    savedReply.getId(),
                    sendUser,
                    receivedUser,
                    NotificationContent.REPLY,
                    topic
            );

            notificationService.sendStructuredNotificationToUser(notification);
        }
        System.out.println("send username: " + sendUsername);
        System.out.println("received username: " + receivedUsername);

        if (savedReply.getParentReplyId() != null && !savedReply.getParentReplyId().equals("")) {
            var parentReply = replyRepository.findById(savedReply.getParentReplyId()).orElse(null);
            if (parentReply != null) {
                User parentUser = parentReply.getUser();
                //parent reply not equal reply
                if (!sendUser.getId().equals(parentUser.getId()) && !parentUser.getId().equals(receivedUser.getId())) {
                    Notification notification = notificationService.createNotification(
                            savedReply.getId(),
                            sendUser,
                            parentUser,
                            NotificationContent.TAG,
                            topic
                    );

                    notificationService.sendStructuredNotificationToUser(notification);
                }
                System.out.println("parent username: " + parentUser.getUsername());

            }
        }

        return replyResponse;
    }

    public ReplyResponse updateReply(String replyId, String content){
        Reply reply = replyRepository.findRepliesById(replyId);
        reply.setContent(content);
        return toReplyResponse(replyRepository.save(reply));
    }
    @Transactional
    public String deleteReply(String replyId){
        replyRepository.deleteRepliesByParentReplyId(replyId);
        replyRepository.deleteById(replyId);
        notificationService.deleteNotification(replyId);
        return replyId;
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

    public ReplyResponse getDetailReply(String replyId){
        Reply reply = replyRepository.findById(replyId).orElseThrow(() -> new AppException(ErrorCode.REPLY_NOTEXISTED));

        return toReplyResponse(reply);
    }

    private ReplyResponse toReplyResponse(Reply reply){
        ReplyResponse replyResponse = replyMapper.toReplyResponse(reply);
        UserGeneral userGeneral = userMapper.toUserGeneral(reply.getUser());
        userGeneral.setAvt(Constant.url + reply.getUser().getProfile().getAvatarUrl());
        replyResponse.setUserGeneral(userGeneral);
        if (reply.getChildReplies() != null && !reply.getChildReplies().isEmpty()) {
            replyResponse.setHasChild(true);
        }
        replyResponse.setOwner(isOwner(reply.getId()));
        return replyResponse;
    }
}
