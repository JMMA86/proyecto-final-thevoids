package org.thevoids.oncologic.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    /**
     * Stores a file and returns the file path
     * 
     * @param file         The file to store
     * @param subdirectory The subdirectory within uploads to store the file
     * @return The relative path to the stored file
     */
    String storeFile(MultipartFile file, String subdirectory);

    /**
     * Deletes a file from the file system
     * 
     * @param filePath The relative path to the file to delete
     */
    void deleteFile(String filePath);

    /**
     * Validates if the file is allowed based on type and size
     * 
     * @param file The file to validate
     * @return true if valid, false otherwise
     */
    boolean isValidFile(MultipartFile file);

    /**
     * Gets the full path to a file
     * 
     * @param relativePath The relative path to the file
     * @return The full path to the file
     */
    String getFullPath(String relativePath);
}
