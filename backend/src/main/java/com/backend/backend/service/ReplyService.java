package com.backend.backend.service;
import com.backend.backend.dto.request.TopicPostRequest;
import com.backend.backend.dto.response.TopicDetailResponse;
import com.backend.backend.entity.SubCategory;
import com.backend.backend.entity.Topic;
import com.backend.backend.entity.User;
import com.backend.backend.exception.AppException;
import com.backend.backend.exception.ErrorCode;
import com.backend.backend.mapper.TopicMapper;
import com.backend.backend.repository.SubCategoryRepository;
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
public class ReplyService {
    UserRepository userRepository;
    SubCategoryRepository subCategoryRepository;
    TopicRepository topicRepository;
    TopicMapper topicMapper;

}
