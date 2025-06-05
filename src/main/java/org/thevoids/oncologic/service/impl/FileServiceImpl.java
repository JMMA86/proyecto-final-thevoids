package org.thevoids.oncologic.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thevoids.oncologic.exception.InvalidOperationException;
import org.thevoids.oncologic.service.FileService;

@Service
public class FileServiceImpl implements FileService {

    @Value("${app.file.upload.dir:uploads}")
    private String uploadDir;

    @Value("${app.file.max-size:10485760}") // 10MB default
    private long maxFileSize;

    private final List<String> allowedContentTypes = Arrays.asList(
            "image/jpeg", "image/png", "image/gif", "image/webp",
            "application/pdf",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "text/plain");

    @Override
    public String storeFile(MultipartFile file, String subdirectory) {
        if (file == null || file.isEmpty()) {
            throw new InvalidOperationException("File cannot be empty");
        }

        if (!isValidFile(file)) {
            throw new InvalidOperationException("Invalid file type or size");
        }

        try {
            // Create upload directory if it doesn't exist
            Path uploadPath = Paths.get(uploadDir, subdirectory);
            Files.createDirectories(uploadPath);

            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String filename = UUID.randomUUID().toString() + fileExtension;

            // Store the file
            Path filePath = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Return relative path
            return subdirectory + "/" + filename;

        } catch (IOException e) {
            throw new InvalidOperationException("Failed to store file: " + e.getMessage());
        }
    }

    @Override
    public void deleteFile(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            return;
        }

        try {
            Path path = Paths.get(uploadDir, filePath);
            Files.deleteIfExists(path);
        } catch (IOException e) {
            // Log the error but don't throw exception as this is cleanup
            System.err.println("Failed to delete file: " + filePath + " - " + e.getMessage());
        }
    }

    @Override
    public boolean isValidFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }

        // Check file size
        if (file.getSize() > maxFileSize) {
            return false;
        }

        // Check content type
        String contentType = file.getContentType();
        return contentType != null && allowedContentTypes.contains(contentType.toLowerCase());
    }

    @Override
    public String getFullPath(String relativePath) {
        if (relativePath == null || relativePath.trim().isEmpty()) {
            return null;
        }
        return Paths.get(uploadDir, relativePath).toString();
    }
}
