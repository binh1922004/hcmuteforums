package com.backend.backend.config;

import com.backend.backend.utils.FileStorageUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StarupConfig {
    @Value("${upload.dir}") // Lấy đường dẫn từ application.properties
    private String uploadDir;
    @Value("${upload.avatar-dir}")
    private String avatarsDir;
    @Value("${upload.cover-dir}")
    private String coverImagesDir;

    @PostConstruct
    public void init() {
        FileStorageUtil.createFolderIfNotExists(uploadDir);
        FileStorageUtil.createFolderIfNotExists(avatarsDir);
        FileStorageUtil.createFolderIfNotExists(coverImagesDir);
    }
}
