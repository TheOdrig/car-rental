package com.akif.shared.infrastructure;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileUploadService {

    String uploadFile(MultipartFile file, String directory);

    void deleteFile(String filePath);

    String generateSecureUrl(String filePath, int expirationMinutes);

    boolean validateFileType(MultipartFile file, List<String> allowedTypes);

    boolean validateFileSize(MultipartFile file, long maxSizeBytes);
}
