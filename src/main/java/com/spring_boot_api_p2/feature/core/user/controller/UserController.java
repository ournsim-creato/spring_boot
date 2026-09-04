package com.spring_boot_api_p2.feature.core.user.controller;

import com.spring_boot_api_p2.base.BaseApi;
// 1. ប្តូរ Import មកកាន់ DTO ថ្មីដែលមាន field profile
import com.spring_boot_api_p2.feature.core.user.dto.response.UserResponse;
import com.spring_boot_api_p2.feature.core.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/{userId}/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseApi<String>> uploadProfileImage(
            @PathVariable Long userId,
            @RequestParam("file") MultipartFile file) {

        String objectKey = userService.uploadProfileImage(userId, file);

        return ResponseEntity.ok(
                BaseApi.<String>builder()
                        .status(true)
                        .code(HttpStatus.OK.value())
                        .message("Success")
                        .timestamp(LocalDateTime.now())
                        .data(objectKey)
                        .build()
        );
    }

    @GetMapping("/{userId}")
    public ResponseEntity<BaseApi<UserResponse>> getById(@PathVariable Long userId) {

        UserResponse userResponse = userService.getById(userId);

        return ResponseEntity.ok(
                BaseApi.<UserResponse>builder()
                        .status(true)
                        .code(HttpStatus.OK.value())
                        .message("Success")
                        .timestamp(LocalDateTime.now())
                        .data(userResponse)
                        .build()
        );
    }
}
