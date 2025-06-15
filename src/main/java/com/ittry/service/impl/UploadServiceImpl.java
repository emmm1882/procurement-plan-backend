package com.ittry.service.impl;

import com.ittry.service.UploadService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadServiceImpl implements UploadService {
    @Override
    public String saveFile(MultipartFile file) {
        // 实际保存逻辑，当前仅返回原始文件名
        return file.getOriginalFilename();
    }
} 