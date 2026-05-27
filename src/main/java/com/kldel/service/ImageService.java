package com.kldel.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageService {

    @Value("${upload.path:upload-data/kldel/uploads/}")
    private String uploadPath;

    public String saveImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        try {
            Path uploadDir = Paths.get(uploadPath);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String filename = UUID.randomUUID().toString() + extension;
            
            Path filePath = uploadDir.resolve(filename);
            Files.copy(file.getInputStream(), filePath);
            
            return "/uploads/" + filename;
            
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
