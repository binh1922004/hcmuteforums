package com.backend.backend.service;

import com.backend.backend.dto.UserGeneral;
import com.backend.backend.dto.request.TopicPostRequest;
import com.backend.backend.dto.request.TopicUpdateRequest;
import com.backend.backend.dto.response.TopicDetailResponse;
import com.backend.backend.entity.SubCategory;
import com.backend.backend.entity.Topic;
import com.backend.backend.entity.TopicImage;
import com.backend.backend.entity.User;
import com.backend.backend.exception.AppException;
import com.backend.backend.exception.ErrorCode;
import com.backend.backend.mapper.TopicMapper;
import com.backend.backend.mapper.UserMapper;
import com.backend.backend.repository.*;
import com.backend.backend.utils.Constant;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class TopicImageService {
    //repo
    TopicRepository topicRepository;
    TopicImageRepository topicImageRepository;
    //
    TopicService topicService;
    @NonFinal
    @Value("${upload.dir}")
    private String uploadDir;
    public boolean holdImages(String topicId, List<String> images) {
        Topic topic = topicRepository.findById(topicId).orElseThrow(() -> new AppException(ErrorCode.TOPIC_NOTEXISTED));
        // Tạo danh sách các TopicImage cần xóa
        List<TopicImage> imagesToRemove = new ArrayList<>();
        for (TopicImage image : topic.getListImages()) {
            String url = Constant.url + image.getImageUrl();
            if (!images.contains(url)) {
                imagesToRemove.add(image);
            }
        }

        // Xóa các TopicImage khỏi listImages
        topic.getListImages().removeAll(imagesToRemove);

        // Lưu Topic để commit thay đổi
        topicRepository.save(topic);
        return true;
    }
    public TopicDetailResponse uploadImages(String topicId, List<MultipartFile> images) {
        Topic topic = topicRepository.findById(topicId).orElseThrow(() -> new AppException(ErrorCode.TOPIC_NOTEXISTED));
        if (images == null || images.isEmpty()) {
            return topicService.getTopicDetail(topicId);
        }
        try{
            for (var image: images) {
                String filename = topic.getId() + "-" + UUID.randomUUID().toString() + ".jpg";
                Path path = Paths.get(uploadDir, filename);
                image.transferTo(new File(path.toAbsolutePath().toString()));
                String imageUrl = uploadDir + filename;
                TopicImage topicImage = TopicImage.builder()
                        .topic(topic)
                        .imageUrl(imageUrl)
                        .build();
                topicImageRepository.save(topicImage);
            }
            return topicService.getTopicDetail(topicId);
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
