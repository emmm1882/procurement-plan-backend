package com.ittry.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    String saveFile(MultipartFile file);
} 