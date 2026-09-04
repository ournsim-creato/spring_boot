package com.spring_boot_api_p2.feature.core;
import org.springframework.web.multipart.MultipartFile;

public interface TestImageService {
    void testImage(MultipartFile file);
}
