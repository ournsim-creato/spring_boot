package com.spring_boot_api_p2.feature.core.user.service;

// ប្តូរមក Import DTO ថ្មីនេះ
import com.spring_boot_api_p2.feature.core.user.dto.response.UserResponse;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    UserResponse getById(Long userId);
    String uploadProfileImage(Long userId, MultipartFile file);
}
