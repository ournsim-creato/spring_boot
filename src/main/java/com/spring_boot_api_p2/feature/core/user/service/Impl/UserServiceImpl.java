package com.spring_boot_api_p2.feature.core.user.service.Impl;

import com.spring_boot_api_p2.domain.entity.User;
import com.spring_boot_api_p2.exception.ResourceNotFoundException;
import com.spring_boot_api_p2.feature.core.user.repository.UserRepository;
import com.spring_boot_api_p2.mapper.UserMapper;
import com.spring_boot_api_p2.feature.core.user.dto.response.UserResponse;
import com.spring_boot_api_p2.feature.core.user.service.UserService;
import com.spring_boot_api_p2.feature.intergration.filestorage.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final FileStorageService fileStorageService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private static final String PROFILE_IMAGE_DIR = "profiles";

    @Override
    public UserResponse getById(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        System.out.println("PROFILE IMAGE FROM DB = " + user.getProfileImage());

        UserResponse response = userMapper.toResponse(user);

        System.out.println("PROFILE IMAGE FROM DTO = " + response.getProfile());

        String profileUrl = null;

        if (user.getProfileImage() != null && !user.getProfileImage().isBlank()) {
            profileUrl = fileStorageService.getFileUrl(
                    user.getProfileImage()
            );
        }

        response.setProfile(profileUrl);

        return response;
    }

    @Override
    public String uploadProfileImage(Long userId, MultipartFile file) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        String objectKey = fileStorageService.replaceImage(
                user.getProfileImage(),
                file,
                PROFILE_IMAGE_DIR
        );

        user.setProfileImage(objectKey);

        userRepository.save(user);

        return objectKey;
    }
}
