package com.spring_boot_api_p2.feature;

import com.spring_boot_api_p2.base.BaseApi;
import com.spring_boot_api_p2.dto.response.RoleImportResult;
import com.spring_boot_api_p2.feature.core.TestImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/test-image")
@RequiredArgsConstructor
public class TestImageController {
    private final TestImageService testService;
    @PostMapping
    public ResponseEntity<?> uploadCurrentProfileImage(@RequestPart("file") MultipartFile file) {
        testService.testImage(file);
        return ResponseEntity.ok(null);
    }
}