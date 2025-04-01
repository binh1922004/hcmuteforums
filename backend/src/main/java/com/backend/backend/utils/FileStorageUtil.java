package com.backend.backend.utils;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileStorageUtil {
    public static void createFolderIfNotExists(String folderPath) {
        Path path = Paths.get(folderPath);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
                System.out.println("📁 Thư mục đã được tạo: " + folderPath);
            } catch (Exception e) {
                System.err.println("❌ Lỗi khi tạo thư mục: " + e.getMessage());
            }
        } else {
            System.out.println("✅ Thư mục đã tồn tại: " + folderPath);
        }
    }
}
